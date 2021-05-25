package com.github.hugovallada.proposta.proposta

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Introspected
data class NovaPropostaRequest(
    @field:NotBlank
    val documento: String,
    @field:Email @field:NotBlank
    val email: String,
    @field:NotBlank
    val nome: String,
    @field:NotBlank
    val cep: String,
    @field:NotNull @field:Positive
    val salario: BigDecimal
){
    fun toModel(endereco: Endereco) : Proposta {
        return Proposta(documento, email, nome, endereco, salario)
    }
}


