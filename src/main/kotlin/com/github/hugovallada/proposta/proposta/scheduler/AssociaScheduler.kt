package com.github.hugovallada.proposta.proposta.scheduler

import com.github.hugovallada.proposta.proposta.PropostaRepository
import com.github.hugovallada.proposta.proposta.cartao.CartaoClient
import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import io.micronaut.scheduling.annotation.Scheduled
import javax.inject.Singleton

@Singleton
class AssociaScheduler(
    private val cartaoClient: CartaoClient,
    private val repository: PropostaRepository
) {

    @Scheduled(fixedDelay = "100s")
    fun associarProposta(){
        val propostas = repository.buscarPropostasElegiveisSemCartao()

        propostas.forEach {
            proposta ->
            if(proposta.situacao == StatusProposta.ELEGIVEL){
                val associarCartaoEProposta = cartaoClient.associarCartaoEProposta(proposta.id.toString())
                val cartao = associarCartaoEProposta.toModel()

                proposta.cartao = cartao
                repository.update(proposta)
            }
        }
    }

}