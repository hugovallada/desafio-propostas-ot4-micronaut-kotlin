package com.github.hugovallada.proposta.proposta.endereco

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("https://viacep.com.br/ws")
interface EnderecoClient {

    @Get("/{cep}/json")
    fun buscarEndereco(@PathVariable cep: String): HttpResponse<EnderecoRequest>

}