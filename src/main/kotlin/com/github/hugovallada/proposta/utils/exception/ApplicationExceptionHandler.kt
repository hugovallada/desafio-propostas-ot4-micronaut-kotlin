package com.github.hugovallada.proposta.utils.exception

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import java.lang.RuntimeException
import java.rmi.server.ServerCloneException
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Produces
@Singleton
@Replaces(ConstraintExceptionHandler::class) // assim consegue substituir o tratador de exceções padrão do Micronaut
class ApplicationExceptionHandler:ExceptionHandler<Exception, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: Exception): HttpResponse<*> {

        when(exception){
            is ConstraintViolationException -> {
                val listaDeMensagens = exception.message?.split(",") ?: return HttpResponse.badRequest(null)
                val body = handleConstraintNotValidException(listaDeMensagens)
                return HttpResponse.badRequest(ApiErrorResponse(body))
            }
        }

        return HttpResponse.serverError(null)
    }

    private fun handleConstraintNotValidException(listaDeMensagens: List<String>): MutableList<ApiError> {
        val body = mutableListOf<ApiError>()

        listaDeMensagens.forEach { lista ->
            val resposta = lista.split(":")
            body.add(
                ApiError(
                    resposta[0].trim().substringAfterLast("."),
                    resposta[1].trim()
                )
            )
        }
        return body
    }


}

data class ApiError(val campo: String, val message: String)
data class ApiErrorResponse(val errors: List<ApiError>)