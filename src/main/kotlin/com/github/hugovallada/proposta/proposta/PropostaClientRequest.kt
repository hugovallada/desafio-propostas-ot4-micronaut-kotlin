package com.github.hugovallada.proposta.proposta

class PropostaClientRequest(proposta: Proposta) {
    val documento = proposta.documento
    val nome = proposta.nome
    val idProposta = proposta.id
}