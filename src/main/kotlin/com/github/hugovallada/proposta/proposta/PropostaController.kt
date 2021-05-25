package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.EnderecoClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Controller("/propostas")
@Validated
class PropostaController(
    private val client: EnderecoClient,
    private val propostaRepository: PropostaRepository
) {

    @Post
    fun cadastrarNovaProposta(@Body @Valid request: NovaPropostaRequest): HttpResponse<Any> {

        propostaRepository.existsByDocumento(request.documento).run {
            if(this) {
                return HttpResponse.badRequest(mapOf(Pair("mensagem", "Já existe uma proposta para esse documento")))
            }
        }

        // fazer tratamento de erros caso o resultado do client seja erro
        val enderecoResponse = client.buscarEndereco(request.cep)

        if (enderecoResponse.body() == null) {
            return HttpResponse.badRequest(mapOf(Pair("mensagem", "Endereço inválido")))
        }


        val proposta = request.toModel(enderecoRequest = enderecoResponse.body()!!)

        propostaRepository.save(proposta).run {
            val uri = UriBuilder.of("/propostas/{id}")
                .expand(mutableMapOf(Pair("id", id)))

            return HttpResponse.created(uri)
        }

    }


}