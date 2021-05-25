package com.github.hugovallada.proposta.proposta

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal

@Introspected
data class NovaPropostaRequest(
    val documento: String,
    val email: String,
    val nome: String,
    val cep: String,
    val salario: BigDecimal
)
