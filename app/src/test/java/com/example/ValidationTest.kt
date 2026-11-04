package com.example

import com.example.util.Validation
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    @Test
    fun `nombre exige al menos 3 caracteres sin contar espacios de los extremos`() {
        assertFalse(Validation.isValidName("  Al "))
        assertTrue(Validation.isValidName("Ana"))
    }

    @Test
    fun `celular exige 7 digitos y acepta separadores`() {
        assertFalse(Validation.isValidPhone("30012"))
        assertFalse(Validation.isValidPhone("abcdefghij"))
        assertTrue(Validation.isValidPhone("300 123 4567"))
        assertTrue(Validation.isValidPhone("+57 300-1234567"))
    }

    @Test
    fun `celular de mas de 20 caracteres se rechaza porque la regla de Firestore lo rechaza`() {
        assertFalse(Validation.isValidPhone("3001234567 3001234567 3001234567"))
    }

    @Test
    fun `correo debe tener arroba y dominio`() {
        assertFalse(Validation.isValidEmail("sin-arroba.com"))
        assertFalse(Validation.isValidEmail("a@b"))
        assertFalse(Validation.isValidEmail("a b@c.com"))
        assertTrue(Validation.isValidEmail(" aspirante@correo.com "))
    }

    @Test
    fun `correo opcional acepta vacio pero no uno mal escrito`() {
        assertTrue(Validation.isValidOptionalEmail(""))
        assertTrue(Validation.isValidOptionalEmail("   "))
        assertFalse(Validation.isValidOptionalEmail("mal"))
    }
}
