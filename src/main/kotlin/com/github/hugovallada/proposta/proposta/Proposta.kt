package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.cartao.Cartao
import com.github.hugovallada.proposta.proposta.endereco.Endereco
import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class Proposta(
    val documento: String,
    val email: String,
    val nome: String,
    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    val endereco: Endereco,
    val salario: BigDecimal
) {

    @Id @GeneratedValue
    var id: UUID? = null

    @Enumerated(value = EnumType.STRING)
    var situacao: StatusProposta? = null

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "cartao_id", referencedColumnName = "id")
    var cartao: Cartao? = null
}