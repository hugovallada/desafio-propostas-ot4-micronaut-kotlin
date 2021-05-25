package com.github.hugovallada.proposta.utils.validators

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [CepValidator::class])
annotation class Cep(val message: String = "O cep é inválido")

@Singleton
class CepValidator : ConstraintValidator<Cep, String> {
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<Cep>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value.isNullOrEmpty()) {
            return true
        }

        value.run {
            return matches("[0-9]{5}-?[\\d]{3}".toRegex())
        }
    }

}
