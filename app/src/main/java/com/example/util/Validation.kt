package com.example.util

/**
 * Validación de formularios de aspirantes. Debe ser al menos tan estricta como firestore.rules
 * (nombre >= 3, celular >= 7 caracteres): un dato que la regla rechaza se pierde sin aviso.
 */
object Validation {
    private val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    const val NAME_ERROR = "Escribe tu nombre completo"
    const val PHONE_ERROR = "Escribe un celular válido (mínimo 7 dígitos)"
    const val EMAIL_ERROR = "Escribe un correo válido, por ejemplo nombre@correo.com"

    fun isValidName(value: String) = value.trim().length >= 3

    fun isValidPhone(value: String) = value.count { it.isDigit() } >= 7 && value.trim().length <= 20

    fun isValidEmail(value: String) = emailRegex.matches(value.trim())

    /** Correo opcional: vacío es válido, pero si se escribe debe tener forma de correo. */
    fun isValidOptionalEmail(value: String) = value.isBlank() || isValidEmail(value)
}
