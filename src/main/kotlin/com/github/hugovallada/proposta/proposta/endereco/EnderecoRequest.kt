package com.github.hugovallada.proposta.proposta.endereco

import javax.print.DocFlavor

data class EnderecoRequest(
    val logradouro: String,
    val uf : String,
    val localidade: String,
) {

}
