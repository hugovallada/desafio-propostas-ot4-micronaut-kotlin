package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.EnderecoClient
import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.util.*
import javax.transaction.Transactional
import javax.validation.Valid

@Controller("/propostas")
@Validated
class PropostaController(
    private val client: EnderecoClient,
    private val propostaRepository: PropostaRepository,
    private val propostaClient: PropostaClient
) {

    @Post
    @Transactional
    fun cadastrarNovaProposta(@Body @Valid request: NovaPropostaRequest): HttpResponse<Any> {

        propostaRepository.existsByDocumento(request.documento).run {
            if (this) {
                return HttpResponse.unprocessableEntity()
            }
        }

        // fazer tratamento de erros caso o resultado do client seja erro
        val enderecoResponse = client.buscarEndereco(request.cep)

        if (enderecoResponse.body() == null) {
            return HttpResponse.badRequest(mapOf(Pair("mensagem", "Endereço inválido")))
        }


        val proposta = request.toModel(enderecoRequest = enderecoResponse.body()!!)

        propostaRepository.save(proposta).run {

            try {
                // Verificar pq o Micronaut continua exibindo erros msm dentro do try/catch
                val situacaoSolicitante = propostaClient.situacaoSolicitante(PropostaClientRequest(this))

                if(situacaoSolicitante.body() == null) return HttpResponse.unprocessableEntity()

                situacao = if (situacaoSolicitante.body()?.resultadoSolicitacao == StatusPropostaClient.SEM_RESTRICAO) {
                    StatusProposta.ELEGIVEL
                } else {
                    StatusProposta.NAO_ELEGIVEL
                }

            } catch (httpException: HttpClientResponseException) {
                situacao = StatusProposta.NAO_ELEGIVEL
            }

            val uri = UriBuilder.of("/propostas/{id}")
                .expand(mutableMapOf(Pair("id", id)))

            return HttpResponse.created(uri)
        }

    }

    @Get("/{id}")
    fun buscarProposta(@PathVariable id: String) : HttpResponse<PropostaResponse> {
        propostaRepository.findById(UUID.fromString(id)).run {
            return if (isEmpty) HttpResponse.notFound() else HttpResponse.ok(PropostaResponse(get()))
        }
    }


}