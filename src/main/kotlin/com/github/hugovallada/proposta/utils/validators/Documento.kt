package com.github.hugovallada.proposta.utils.validators

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD

/**
 * Este validador, tem a responsabilidade de validar apenas o formato
 */
@MustBeDocumented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = [DocumentoValidator::class])
annotation class Documento(val message: String = "O formato do documento não é valido")

@Singleton
class DocumentoValidator : ConstraintValidator<Documento, String> {
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<Documento>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value.isNullOrEmpty()) {
            return true
        }

        value.run {
            return matches("(^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}\$)|(^\\d{2}\\.?\\d{3}\\.?\\d{3}/\\d{4}-?\\d{2}\$)".toRegex())
        }
    }

}
