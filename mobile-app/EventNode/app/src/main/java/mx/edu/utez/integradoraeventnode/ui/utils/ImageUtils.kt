package mx.edu.utez.integradoraeventnode.ui.utils

import android.graphics.BitmapFactory
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
