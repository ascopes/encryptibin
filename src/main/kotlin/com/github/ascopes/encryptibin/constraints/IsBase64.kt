package com.github.ascopes.encryptibin.constraints

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [Base64Validator::class])
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class IsBase64(
    val message: String = "{encryptibin.constraints.IsBase64.message}",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
)

internal class Base64Validator : ConstraintValidator<IsBase64, String> {
    override fun isValid(text: String?, ctx: ConstraintValidatorContext): Boolean {
        if (text == null) {
            return true
        }

        var padding = 0
        text.forEach {
            if (padding == 2) {
                // Too many padding chars.
                return false
            }

            if (padding > 0) {
                if (it == '=') {
                    ++padding
                } else {
                    // Non padding char after padding char.
                    return false
                }
            }

            when (it) {
                in 'A'..'Z' -> {}
                in 'a'..'z' -> {}
                in '0'..'9' -> {}
                '+' -> {}
                '-' -> {}
                '=' -> padding = 1
                else -> return false
            }
        }

        return true
    }

}