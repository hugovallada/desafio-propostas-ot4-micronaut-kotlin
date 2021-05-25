package com.github.hugovallada.proposta.proposta

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9999")
interface PropostaClient {

    @Post("/api/solicitacao")
    fun situacaoSolicitante(@Body proposta: PropostaClientRequest) : HttpResponse<PropostaClientResponse>

}