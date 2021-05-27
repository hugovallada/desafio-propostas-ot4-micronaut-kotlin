package com.github.hugovallada.proposta.proposta.scheduler

import com.github.hugovallada.proposta.proposta.Proposta
import com.github.hugovallada.proposta.proposta.PropostaRepository
import com.github.hugovallada.proposta.proposta.cartao.CartaoClient
import com.github.hugovallada.proposta.proposta.cartao.CartaoClientResponse
import com.github.hugovallada.proposta.proposta.endereco.Endereco
import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
internal class AssociaSchedulerTest {


    @field:Inject
    lateinit var cartaoClient: CartaoClient

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var propostaRepository: PropostaRepository

    lateinit var proposta: Proposta

    @BeforeEach
    internal fun setUp() {
        val endereco = Endereco("Tibia","RJ","999", "Rio de Janeiro")
        proposta = Proposta("43180393299","hugo@email.com","Hugo", endereco,BigDecimal(2500))
    }

    @Test
    internal fun `deve retornar sucesso quando a proposta for elegivel`() {
        // Cenário
        proposta.situacao = StatusProposta.ELEGIVEL
        propostaRepository.save(proposta)

        // Ação
        Mockito.`when`(cartaoClient.associarCartaoEProposta(proposta.id.toString()))
            .thenReturn((geraCartaoClienteResponse()))

        val associarCartaoEProposta = cartaoClient.associarCartaoEProposta(proposta.id.toString())

        assertNotNull(associarCartaoEProposta)
        val cartao = associarCartaoEProposta.toModel()
        assertNotNull(cartao)
        assertTrue(cartao.id == associarCartaoEProposta.id)
        proposta.cartao = cartao

        assertTrue(proposta.cartao!!.id == cartao.id)

    }

    @Test
    internal fun `deve retornar uma excecao quando um status diferente de 2xx for retornado no client`() {
        proposta.situacao = StatusProposta.ELEGIVEL
        propostaRepository.save(proposta)

        Mockito.`when`(cartaoClient.associarCartaoEProposta(proposta.id.toString()))
            .thenThrow(HttpClientResponseException::class.java)

        assertThrows(HttpClientResponseException::class.java){
            cartaoClient.associarCartaoEProposta(proposta.id.toString())
        }
    }

    @MockBean(CartaoClient::class)
    fun mockCartaoClient(): CartaoClient? {
        return Mockito.mock(CartaoClient::class.java)
    }

    fun geraCartaoClienteResponse(): CartaoClientResponse {
        return CartaoClientResponse(
            "2903-9203-3458-2378",
            LocalDateTime.now(),
            proposta.nome,
            proposta.salario * BigDecimal.TEN,
            proposta.id.toString()
        )
    }

}





