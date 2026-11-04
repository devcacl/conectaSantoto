package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CampusEvent
import com.example.data.model.EventRegistration
import com.example.ui.components.qrMatrix
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class EventTicketTest {

    /** Rasteriza la matriz igual que un lector la vería: módulos negros sobre blanco con zona de silencio. */
    private fun decode(content: String): String {
        val matrix = qrMatrix(content)
        val scale = 6
        val quiet = 4
        val side = (matrix.width + quiet * 2) * scale
        val pixels = IntArray(side * side) { 0xFFFFFFFF.toInt() }
        for (y in 0 until matrix.height) for (x in 0 until matrix.width) {
            if (!matrix[x, y]) continue
            for (dy in 0 until scale) for (dx in 0 until scale) {
                pixels[((y + quiet) * scale + dy) * side + (x + quiet) * scale + dx] = 0xFF000000.toInt()
            }
        }
        val bitmap = BinaryBitmap(HybridBinarizer(RGBLuminanceSource(side, side, pixels)))
        return QRCodeReader().decode(bitmap).text
    }

    @Test
    fun `el QR del boleto se decodifica y devuelve el codigo exacto`() {
        assertEquals("USTA-EVT-3F9A1C2B", decode("USTA-EVT-3F9A1C2B"))
    }

    private fun viewModel() = SantotoViewModel(ApplicationProvider.getApplicationContext<Application>())

    private val event = CampusEvent(id = 1, title = "Open Day", dateBadge = "18 OCT", time = "9:00", location = "Campus", category = "Feria", description = "")
    private val registration = EventRegistration(id = 1, eventId = 1, eventTitle = "Open Day", fullName = "Ana Pérez", phone = "3001234567", email = "a@b.co")

    @Test
    fun `volver desde el boleto lleva a Eventos y no al formulario`() {
        val vm = viewModel()
        vm.navigateTo(Screen.Events)
        vm.navigateTo(Screen.EventConfirmRegistration(event))
        vm.navigateTo(Screen.EventTicket(registration, event))

        vm.navigateBack()

        assertEquals(Screen.Events, vm.currentScreen.value)
    }

    @Test
    fun `desde Inicio la inscripcion tambien vuelve a Eventos y luego a Inicio`() {
        val vm = viewModel()
        vm.navigateTo(Screen.EventConfirmRegistration(event))
        vm.navigateTo(Screen.EventTicket(registration, event))

        assertTrue(vm.navigateBack())
        assertEquals(Screen.Events, vm.currentScreen.value)

        assertTrue(vm.navigateBack())
        assertEquals(Screen.Home, vm.currentScreen.value)
    }
}
