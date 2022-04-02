package com.github.ascopes.encryptibin.constraints

import com.github.ascopes.encryptibin.models.props.EncryptionProperties
import java.time.Duration
import javax.validation.Constraint
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired

@Constraint(validatedBy = [MaxDurationValidator::class])
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class MaxDuration(
    val message: String = "{encryptibin.constraints.MaxDuration.message}",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
)

internal class MaxDurationValidator : HibernateConstraintValidator<MaxDuration, Long> {
    @Autowired
    private lateinit var encryptionProperties: EncryptionProperties

    override fun isValid(duration: Long?, ctx: ConstraintValidatorContext): Boolean {
        if (duration != null && duration > encryptionProperties.maxExpireAfter.toSeconds()) {
            (ctx as HibernateConstraintValidatorContext)
                .addMessageParameter("maxDuration", maxExpireAfterString())

            return false
        }

        return true
    }

    private fun maxExpireAfterString() = arrayOf(
        duration(Duration::toDaysPart, "day"),
        duration(Duration::toHoursPart, "hour"),
        duration(Duration::toMinutesPart, "minute"),
        duration(Duration::toSecondsPart, "second")
    ).mapNotNull { it(encryptionProperties.maxExpireAfter) }.joinToString()

    private fun duration(mapper: (Duration) -> Number, name: String) = { duration: Duration ->
        when (val part = mapper(duration)) {
            0 -> null
            1 -> "1 $name"
            else -> "$part ${name}s"
        }
    }
}