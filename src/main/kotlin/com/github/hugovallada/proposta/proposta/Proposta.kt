package com.github.hugovallada.proposta.proposta

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class Proposta(
    val documento: String,
    val email: String,
    val nome: String,
    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    val endereco: Endereco,
    val salario: BigDecimal
) {

    @Id @GeneratedValue
    var id: UUID? = null
}