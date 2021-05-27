package com.github.hugovallada.proposta.proposta.cartao

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8888")
interface CartaoClient {

    @Get("/api/cartoes?idProposta={idProposta}")
    fun associarCartaoEProposta(@QueryValue idProposta: String) : CartaoClientResponse

}