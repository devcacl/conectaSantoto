package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoRed
import com.example.ui.theme.SantotoTextBody
import com.example.ui.theme.SantotoTextSecondary
import com.example.ui.theme.SantotoYellow
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** Umbral desde el cual se avisa que quedan pocos cupos. */
const val LOW_SPOTS_THRESHOLD = 10

/** Fila icono + texto para hora, lugar, fecha. */
@Composable
fun EventDetailRow(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.Top, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(16.dp).padding(top = 2.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = SantotoTextBody)
    }
}

/**
 * Cupos del evento. Los cupos se descuentan en cada teléfono (no hay conteo compartido todavía),
 * por eso no se rotulan como "tiempo real".
 */
@Composable
fun SpotsIndicator(available: Int, capacity: Int, modifier: Modifier = Modifier) {
    val low = available <= LOW_SPOTS_THRESHOLD
    val fraction = (available.toFloat() / capacity.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SantotoBg, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when {
                    available <= 0 -> "Cupos agotados"
                    low -> "¡Últimos cupos!"
                    else -> "Cupos disponibles"
                },
                style = MaterialTheme.typography.labelMedium,
                color = if (low) SantotoRed else SantotoTextSecondary
            )
            Text(
                text = "$available de $capacity",
                style = MaterialTheme.typography.labelMedium,
                color = if (low) SantotoRed else SantotoNavy
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (low) SantotoRed else SantotoYellow,
            trackColor = SantotoOutline
        )
    }
}

/** Matriz del QR sin margen (la zona de silencio la aporta el contenedor blanco). */
internal fun qrMatrix(content: String): BitMatrix = QRCodeWriter().encode(
    content,
    BarcodeFormat.QR_CODE,
    0,
    0,
    mapOf(
        EncodeHintType.MARGIN to 0,
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M
    )
)

/** Código QR real (ZXing), legible por cualquier lector. Contraste máximo: negro sobre blanco. */
@Composable
fun QrCode(content: String, modifier: Modifier = Modifier) {
    val matrix = remember(content) { qrMatrix(content) }
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val cells = matrix.width
        val cell = size.width / cells
        for (y in 0 until cells) {
            for (x in 0 until cells) {
                if (matrix[x, y]) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(x * cell, y * cell),
                        size = Size(cell + 0.5f, cell + 0.5f) // solapa medio píxel: evita líneas finas
                    )
                }
            }
        }
    }
}
