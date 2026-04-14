package mx.edu.utez.integradoraeventnode.ui.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun assetImageBitmap(fileName: String): ImageBitmap {
    val context = LocalContext.current
    return remember(fileName) {
        try {
            BitmapFactory.decodeStream(context.assets.open(fileName)).asImageBitmap()
        } catch (e: Exception) {
            ImageBitmap(1, 1)
        }
    }
}

/**
 * Decodifica una imagen base64 (con o sin prefijo data:image/...) a ImageBitmap.
 * Retorna null si el string es inválido.
 */
fun decodeBase64Image(base64String: String?): ImageBitmap? {
    if (base64String.isNullOrBlank()) return null
    return try {
        val clean = if (base64String.contains(",")) {
            base64String.substringAfter(",")
        } else {
            base64String
        }
        val bytes = Base64.decode(clean, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
