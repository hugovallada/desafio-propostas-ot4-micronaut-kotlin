package com.github.hugovallada.proposta.biometria

import com.github.hugovallada.proposta.proposta.cartao.Cartao
import com.github.hugovallada.proposta.proposta.cartao.CartaoRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid

@Controller("/biometrias")
@Validated
class BiometricaController(
    private val cartaoRepository: CartaoRepository,
    private val biometriaRepository: BiometriaRepository
) {


    @Post("/cartao/{idCartao}")
    fun cadastrarNovaBiometria(
        @PathVariable idCartao: String,
        @Body @Valid request: NovaBiometriaRequest
    ) : HttpResponse<Any> {

        cartaoRepository.findById(idCartao)
            .let {
                cartaoOpt ->
                if(cartaoOpt.isEmpty) {
                    return HttpResponse.notFound()
                }

                cartaoOpt.get().run {
                    val biometria = biometriaRepository.save(request.toModel(this))
                    val uri = UriBuilder.of("/biometrias/{id}")
                        .expand(mutableMapOf(Pair("id", biometria.id)))

                    return HttpResponse.created(uri)
                }
            }
    }

}