package com.github.hugovallada.proposta.proposta.endereco

class EnderecoResponse(endereco: Endereco) {
    val logradouro = endereco.logradouro
    val uf = endereco.uf
    val numero = endereco.numero
    val localidade = endereco.localidade
}