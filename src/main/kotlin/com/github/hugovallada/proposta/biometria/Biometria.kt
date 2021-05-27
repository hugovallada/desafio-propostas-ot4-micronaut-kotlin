package com.github.hugovallada.proposta.biometria

import com.github.hugovallada.proposta.proposta.cartao.Cartao
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Biometria(
    val fingerPrint: String,
    @ManyToOne
    val cartao: Cartao,
) {
    @CreationTimestamp
    var dataCadastro: LocalDateTime? = null

    @Id @GeneratedValue
    var id: UUID? = null
}