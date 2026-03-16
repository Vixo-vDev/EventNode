package mx.edu.utez.integradoraeventnode.ui.screens.student.agenda

import android.graphics.BitmapFactory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onViewQr: () -> Unit = {},
    onViewDetail: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Mis eventos",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Próximos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2F6FED)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF2F6FED))
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Pasados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF8B8B8B)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.Transparent)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Hoy, 24 de Octubre",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF1F1F1F)
                )
                Spacer(modifier = Modifier.height(10.dp))
                AgendaCard(
                    title = "Charla tecnológica: IA en el campus",
                    time = "10:00 AM - 12:30 PM",
                    location = "Auditorio Principal, Campus Central",
                    tag = "EN VIVO",
                    imageColor = Color(0xFF9B7A4A),
                    onViewQr = onViewQr,
                    onViewDetail = onViewDetail
                )
                Spacer(modifier = Modifier.height(12.dp))
                AgendaCard(
                    title = "Workshop: Desarrollo Web Sostenible",
                    time = "03:00 PM - 05:00 PM",
                    location = "Laboratorio de Cómputo B2",
                    tag = "",
                    imageColor = Color(0xFF8CB1A0),
                    onViewQr = onViewQr,
                    onViewDetail = onViewDetail
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Mañana, 25 de Octubre",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF1F1F1F)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp)
                        .clickable { onViewDetail() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE7F0FF))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("25", style = MaterialTheme.typography.titleSmall, color = Color(0xFF2F6FED))
                            Text("OCT", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2F6FED))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Feria de Empleo 2024",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "9:00 AM - Explanada Norte",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6C6C6C)
                            )
                            Text(
                                text = "Próximamente",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF6C6C6C)
                            )
                        }
                        Text(
                            text = "Ver detalles",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2F6FED),
                            modifier = Modifier.clickable { onViewDetail() }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AgendaBottomNav(onHome = onHome, onDiplomas = onDiplomas, onProfile = onProfile)
            }
        }
    }
}

@Composable
private fun AgendaCard(
    title: String,
    time: String,
    location: String,
    tag: String,
    imageColor: Color,
    onViewQr: () -> Unit,
    onViewDetail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .clickable { onViewDetail() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(imageColor)
            ) {
                if (tag.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE6F0FF))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = tag, style = MaterialTheme.typography.labelSmall, color = Color(0xFF2F6FED))
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = time, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6C6C6C))
                Text(text = location, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6C6C6C))
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onViewQr,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        bitmap = assetImageBitmap("qr-scan.png"),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver QR de Acceso")
                }
            }
        }
    }
}

@Composable
private fun AgendaBottomNav(onHome: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF5F6FA),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(label = "Inicio", selected = false, onClick = onHome)
            BottomNavItem(label = "Agenda", selected = true, onClick = {})
            BottomNavItem(label = "Diplomas", selected = false, onClick = onDiplomas)
            BottomNavItem(label = "Perfil", selected = false, onClick = onProfile)
        }
    }
}

@Composable
private fun BottomNavItem(label: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B)
    val tint = ColorFilter.tint(if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            bitmap = assetImageBitmap(
                when (label) {
                    "Inicio" -> "home.png"
                    "Agenda" -> "book-open-reader.png"
                    "Diplomas" -> "diploma.png"
                    else -> "user.png"
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            colorFilter = tint
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaScreenPreview() {
    IntegradoraEventNodeTheme {
        AgendaScreen()
    }
}
