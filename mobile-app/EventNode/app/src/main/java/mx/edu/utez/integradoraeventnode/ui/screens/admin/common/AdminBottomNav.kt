package mx.edu.utez.integradoraeventnode.ui.screens.admin.common

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun AdminBottomNav(
    selected: String,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdminNavItem("Inicio", "home.png", selected == "Inicio", onHome)
            AdminNavItem("Agenda", "book-open-reader.png", selected == "Agenda", onAgenda)
            AdminNavItem("Escanear", "qr-scan.png", selected == "Escanear", onEscanear)
            AdminNavItem("Diplomas", "diploma.png", selected == "Diplomas", onDiplomas)
            AdminNavItem("Analitica", "chart-histogram.png", selected == "Analitica", onAnalitica)
            AdminNavItem("Perfil", "user.png", selected == "Perfil", onProfile)
        }
    }
}

@Composable
private fun AdminNavItem(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            bitmap = assetImageBitmap(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = color, fontSize = 8.sp)
    }
}
