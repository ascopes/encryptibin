package com.github.ascopes.encryptibin.constraints

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [UuidValidator::class])
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class IsUuid(
    val message: String = "{encryptibin.constraints.IsUuid.message}",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
)

internal class UuidValidator : ConstraintValidator<IsUuid, String> {
    private val pattern = """^[0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}$"""
        .toRegex(RegexOption.IGNORE_CASE)

    override fun isValid(uuid: String?, ctx: ConstraintValidatorContext) =
        uuid == null || pattern.matches(uuid)
}