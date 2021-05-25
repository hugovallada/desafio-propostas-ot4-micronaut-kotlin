package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.Endereco
import com.github.hugovallada.proposta.proposta.endereco.EnderecoRequest
import com.github.hugovallada.proposta.utils.validators.Cep
import com.github.hugovallada.proposta.utils.validators.Documento
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Introspected
data class NovaPropostaRequest(
    @field:NotBlank @field:Documento
    val documento: String,
    @field:Email @field:NotBlank
    val email: String,
    @field:NotBlank
    val nome: String,
    @field:NotBlank @field:Cep
    val cep: String,
    @field:NotNull @field:Positive
    val salario: BigDecimal,
    @field:NotBlank
    val numero: String
){
    fun toModel(enderecoRequest: EnderecoRequest) : Proposta {
        val endereco = Endereco(enderecoRequest.logradouro, enderecoRequest.uf,numero, enderecoRequest.localidade)
        return Proposta(documento, email, nome, endereco, salario)
    }
}


