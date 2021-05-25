package com.github.hugovallada.proposta.proposta

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Endereco(
    val logradouro: String,
    val uf: String,
    val numero: String,
    val localidade: String
){

    @Id @GeneratedValue
    var id: UUID? = null

}