package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.EnderecoResponse
import io.micronaut.core.annotation.Introspected

class PropostaResponse(proposta: Proposta){
    //val documento = proposta.documento
    //val nome = proposta.nome
    val id = proposta.id
    val email = proposta.email
    val salario = proposta.salario
    val situacao = proposta.situacao
    val cartaoId = proposta.cartao?.id
    //val endereco = EnderecoResponse(proposta.endereco)
}
