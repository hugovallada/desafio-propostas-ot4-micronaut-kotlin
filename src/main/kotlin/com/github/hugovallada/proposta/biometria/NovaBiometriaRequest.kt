package com.github.hugovallada.proposta.biometria

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.hugovallada.proposta.proposta.cartao.Cartao
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
data class NovaBiometriaRequest(
    @field:NotBlank
    val fingerPrint: String
){
    fun toModel(cartao: Cartao): Biometria {
        return Biometria(fingerPrint = fingerPrint, cartao = cartao)
    }
}
