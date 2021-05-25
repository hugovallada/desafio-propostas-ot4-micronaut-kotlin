package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.Endereco
import com.github.hugovallada.proposta.proposta.endereco.EnderecoClient
import com.github.hugovallada.proposta.proposta.endereco.EnderecoRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import javax.inject.Inject

@MicronautTest
internal class PropostaControllerTest{

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    lateinit var propostaRequest: NovaPropostaRequest

    @field:Inject
    lateinit var repository: PropostaRepository

    @field:Inject
    lateinit var enderecoClient: EnderecoClient

    lateinit var proposta: Proposta

    val REGEX_RETURN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"

    @BeforeEach
    internal fun setUp() {
        val endereco = Endereco(logradouro = "Rua Tibiriça", uf = "SP", numero = "688", "Sertãozinho")
        proposta = Proposta("44407669539","email@email.com","Hugo", Endereco("Campos","SP","66","RP"), BigDecimal(2500))
        repository.save(proposta)
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @Test
    internal fun `proposta deve ser cadastrada quando dados válidos forem passados`() {
        // Cenário
        propostaRequest = NovaPropostaRequest("44407669538","email@email.com","Hugo","14000090" , BigDecimal(2500), "688")
        val request = HttpRequest.POST("/propostas", propostaRequest)
        val enderecoRequest = EnderecoRequest(logradouro = "Rua Tibiriça", uf = "SP",localidade =  "Sertãozinho")

        // Ação
        Mockito.`when`(enderecoClient.buscarEndereco(propostaRequest.cep))
            .thenReturn(HttpResponse.ok(enderecoRequest))

        val response = client.toBlocking().exchange(request, Any::class.java)

        // Validação
        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/propostas/$REGEX_RETURN".toRegex()))
    }


    @Test
    internal fun `um status 422 deve ser retornado quando um documento já cadastrado for passado`() {
        propostaRequest = NovaPropostaRequest("44407669539","email@email.com","Hugo","14000090" , BigDecimal(2500), "688")

        //Cenário
        val request = HttpRequest.POST("/propostas", propostaRequest)

        assertThrows(HttpClientResponseException::class.java) {
            val response = client.toBlocking().exchange(request, Any::class.java)
        }.run {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
        }



    }

    @MockBean(EnderecoClient::class)
    fun mockEndereco() : EnderecoClient? {
        return Mockito.mock(EnderecoClient::class.java)
    }
}