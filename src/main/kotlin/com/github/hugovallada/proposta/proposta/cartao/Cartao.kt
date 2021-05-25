package com.github.hugovallada.proposta.proposta.cartao

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Cartao(
    @Id
    val id: String,
    val emitidoEm: LocalDateTime,
    val titular: String,
    val limite: BigDecimal
) {
}