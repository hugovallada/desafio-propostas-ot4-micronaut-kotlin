package com.github.hugovallada.proposta.proposta.scheduler

import com.github.hugovallada.proposta.proposta.PropostaRepository
import com.github.hugovallada.proposta.proposta.cartao.CartaoClient
import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AssociaScheduler(
    private val cartaoClient: CartaoClient,
    private val repository: PropostaRepository
) {

    private val LOG = LoggerFactory.getLogger(AssociaScheduler::class.java.simpleName)

    //@Scheduled(fixedDelay = "100s")
    fun associarProposta() {
        val propostas = repository.buscarPropostasElegiveisSemCartao()

        propostas.forEach { proposta ->
            if (proposta.situacao == StatusProposta.ELEGIVEL) {
                try {
                    val associarCartaoEProposta = cartaoClient.associarCartaoEProposta(proposta.id.toString())
                    val cartao = associarCartaoEProposta.toModel()

                    proposta.cartao = cartao
                    repository.update(proposta)
                } catch (exception: HttpClientResponseException) {
                    LOG.info("Um erro aconteceu: ${exception.message}")
                }
            }
        }
    }

    @Scheduled(fixedDelay = "100s")
    fun associarPropostasFuncional() {
        repository.buscarPropostasElegiveisSemCartao()
            .run {
                forEach { proposta ->
                    if (proposta.situacao == StatusProposta.ELEGIVEL) {
                        try {
                            cartaoClient.associarCartaoEProposta(proposta.id.toString())
                                .run {
                                    proposta.cartao = toModel()
                                    repository.update(proposta)
                                }
                        } catch (exception: HttpClientResponseException) {
                            LOG.info("Uma exceção ocorreu, cartão não cadastrado: ${exception.message}")
                        }
                    }
                }
            }
    }

}