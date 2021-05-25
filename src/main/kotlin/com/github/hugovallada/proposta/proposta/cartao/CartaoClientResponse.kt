package com.github.hugovallada.proposta.proposta.cartao

import java.math.BigDecimal
import java.time.LocalDateTime

data class CartaoClientResponse(
    val id: String,
    val emitidoEm: LocalDateTime,
    val titular: String,
    val limite: BigDecimal,
    val idProposta: String
)
