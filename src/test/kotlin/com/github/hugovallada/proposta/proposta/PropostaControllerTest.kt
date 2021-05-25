package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.Endereco
import com.github.hugovallada.proposta.proposta.endereco.EnderecoClient
import com.github.hugovallada.proposta.proposta.endereco.EnderecoRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
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
    lateinit var enderecoClient: EnderecoClient

    val REGEX_RETURN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"

    @BeforeEach
    internal fun setUp() {
        val endereco = Endereco(logradouro = "Rua Tibiriça", uf = "SP", numero = "688", "Sertãozinho")
        propostaRequest = NovaPropostaRequest("44407669539","email@email.com","Hugo","14000090" , BigDecimal(2500), "688")
    }

    @Test
    internal fun propostaDeveSerElegivelCasoUmCpfValidoSejaPassado() {
        // Cenário
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

    @MockBean(EnderecoClient::class)
    fun mockEndereco() : EnderecoClient? {
        return Mockito.mock(EnderecoClient::class.java)
    }
}