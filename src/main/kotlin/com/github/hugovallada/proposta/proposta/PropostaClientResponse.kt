package com.github.hugovallada.proposta.proposta

data class PropostaClientResponse(
    val documento: String,
    val nome: String,
    val idProposta: String,
    val resultadoSolicitacao: StatusPropostaClient
)