package com.github.hugovallada.proposta.biometria

import com.github.hugovallada.proposta.proposta.Proposta
import com.github.hugovallada.proposta.proposta.PropostaRepository
import com.github.hugovallada.proposta.proposta.cartao.Cartao
import com.github.hugovallada.proposta.proposta.endereco.Endereco
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
internal class BiometricaControllerTest {


    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var propostaRepository: PropostaRepository

    lateinit var cartao: Cartao
    lateinit var proposta: Proposta

    val REGEX_RETURN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}" // regex para UUID



    @BeforeEach
    internal fun setUp() {
        val endereco = Endereco("Tibia","RJ","666","Rio de Janeiro")
        proposta = Proposta("43990909023", "email@email.com", "Teste", endereco, BigDecimal(2500))
        propostaRepository.save(proposta)
        cartao = Cartao("3706-0598-0023-12323", LocalDateTime.now(),"Teste", BigDecimal(25000))
        proposta.cartao = cartao
        propostaRepository.update(proposta)
    }

    @Test
    internal fun `deve cadastrar uma biometria quando um cartao for valido`() {
        val request = HttpRequest.POST("/biometrias/cartao/${cartao.id}", NovaBiometriaRequest("é uma base 64"))
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/biometrias/$REGEX_RETURN".toRegex()))
    }


    @Test
    internal fun `deve retornar um status 404 quando o cartao nao existir`() {
        val request = HttpRequest.POST("/biometrias/cartao/3706-0598-0023-12352", NovaBiometriaRequest("é uma base 64"))

        assertThrows(HttpClientResponseException::class.java) {
            val response = client.toBlocking().exchange(request, Any::class.java)
        }.run {
            assertEquals(HttpStatus.NOT_FOUND, status)
        }
    }

    @Test
    internal fun `deve retornar um status 400 caso a fingerprint passada seja invalida`() {
        val request = HttpRequest.POST("/biometrias/cartao/3706-0598-0023-12352", NovaBiometriaRequest(""))
        assertThrows(HttpClientResponseException::class.java){
            val response = client.toBlocking().exchange(request, Any::class.java)
        }.run {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }

    }
}