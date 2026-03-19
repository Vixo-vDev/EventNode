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
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import android.util.Base64

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onViewQr: () -> Unit = {},
    onViewDetail: (Int) -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var enrolledEvents by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE)
    val usuarioId = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""
    val bearerToken = if (token.isNotEmpty()) "Bearer $token" else ""

    fun decodeBase64Image(base64Str: String?): ImageBitmap? {
        if (base64Str.isNullOrEmpty()) return null
        return try {
            val cleanBase64 = if (base64Str.contains(",")) base64Str.substringAfter(",") else base64Str
            val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(Unit) {
        if (usuarioId != -1 && bearerToken.isNotEmpty()) {
            try {
                val response = ApiClient.apiService.listarMisEventos(bearerToken, usuarioId)
                if (response.isSuccessful) {
                    val allEvents = response.body() ?: emptyList()
                    // Filter only those where inscripcionEstado == "ACTIVO" to be safe
                    enrolledEvents = allEvents.filter { it["inscripcionEstado"] == "ACTIVO" }
                } else {
                    errorMessage = "Error al cargar tu agenda"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión"
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
            errorMessage = "No has iniciado sesión correctamente"
        }
    }

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
                // EVENT CARDS
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Cargando tu agenda...")
                    }
                } else if (errorMessage != null) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                } else if (enrolledEvents.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("No estás inscrito a ningún evento aún.", color = Color.Gray)
                    }
                } else {
                    enrolledEvents.forEach { ev ->
                        val eventId = (ev["idEvento"] as? Double)?.toInt() ?: -1
                        val nombre = ev["nombre"] as? String ?: "Evento"
                        val fechaInicio = ev["fechaInicio"] as? String ?: ""
                        val ubicacion = ev["ubicacion"] as? String ?: "Varias ubicaciones"
                        val bannerBase64 = ev["banner"] as? String

                        val dateStr = if (fechaInicio.length >= 16) {
                            fechaInicio.substring(0, 10).replace("-", "/") + " • " + fechaInicio.substring(11, 16)
                        } else {
                            fechaInicio
                        }

                        AgendaCard(
                            title = nombre,
                            time = dateStr,
                            location = ubicacion,
                            tag = "EN VIVO",
                            imageColor = Color(0xFF9B7A4A),
                            bannerBase64 = bannerBase64,
                            bannerDecoder = ::decodeBase64Image,
                            onViewQr = onViewQr,
                            onViewDetail = { onViewDetail(eventId) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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
    bannerBase64: String? = null,
    bannerDecoder: (String?) -> ImageBitmap? = { null },
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
                // Background Image
                val decodedBanner = bannerDecoder(bannerBase64)
                if (decodedBanner != null) {
                    Image(
                        bitmap = decodedBanner,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        alpha = 0.6f
                    )
                }

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
