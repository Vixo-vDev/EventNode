package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

@Composable
fun AdminAgendaScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {},
    onViewDetail: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var enVivoEvents by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }
    var proximosEvents by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""
                if (token.isNotEmpty()) {
                    val response = ApiClient.apiService.getEventosFiltrados("Bearer $token")
                    if (response.isSuccessful) {
                        val all = (response.body() ?: emptyList()).filter {
                            it.estado != "CANCELADO" && it.estado != "FINALIZADO"
                        }
                        val now = LocalDateTime.now()
                        enVivoEvents = all.filter { ev ->
                            val inicio = parseDateTime(ev.fechaInicio)
                            val fin = parseDateTime(ev.fechaFin)
                            inicio != null && fin != null && !inicio.isAfter(now) && !fin.isBefore(now)
                        }
                        proximosEvents = all.filter { ev ->
                            val inicio = parseDateTime(ev.fechaInicio)
                            inicio != null && inicio.isAfter(now)
                        }
                    }
                }
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Mis Eventos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (enVivoEvents.isEmpty() && proximosEvents.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay eventos activos ni próximos.", color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    } else {
                        // ── EN VIVO ──
                        if (enVivoEvents.isNotEmpty()) {
                            SectionLabel(label = "EN VIVO", color = Color(0xFFE53935))
                            Spacer(modifier = Modifier.height(12.dp))
                            enVivoEvents.forEach { event ->
                                AdminAgendaCard(event = event, isLive = true, onDetail = { onViewDetail(event.idEvento) })
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // ── PRÓXIMOS ──
                        if (proximosEvents.isNotEmpty()) {
                            SectionLabel(label = "PRÓXIMOS", color = Color(0xFF2F6FED))
                            Spacer(modifier = Modifier.height(12.dp))
                            proximosEvents.forEach { event ->
                                AdminAgendaCard(event = event, isLive = false, onDetail = { onViewDetail(event.idEvento) })
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AdminBottomNav(
                    selected = "Agenda",
                    onHome = onHome,
                    onAgenda = {},
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }
}

private fun parseDateTime(str: String?): LocalDateTime? {
    if (str.isNullOrEmpty()) return null
    return try {
        LocalDateTime.parse(str.take(19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
    } catch (e: Exception) { null }
}

@Composable
private fun SectionLabel(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun AdminAgendaCard(
    event: EventoResponse,
    isLive: Boolean,
    onDetail: () -> Unit
) {
    val timeRange = try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val start = LocalDateTime.parse(event.fechaInicio.take(19), formatter)
        val end = LocalDateTime.parse(event.fechaFin.take(19), formatter)
        "${start.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${end.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    } catch (e: Exception) { "Hora no disponible" }

    val bannerBitmap: ImageBitmap? = remember(event.banner) {
        if (event.banner.isNullOrEmpty()) return@remember null
        try {
            val clean = if (event.banner!!.contains(",")) event.banner!!.substringAfter(",") else event.banner!!
            val bytes = android.util.Base64.decode(clean, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) { null }
    }

    val accentColor = if (isLive) Color(0xFFB71C1C) else Color(0xFF2F6FED)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(accentColor.copy(alpha = 0.8f))
            ) {
                if (bannerBitmap != null) {
                    Image(
                        bitmap = bannerBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = if (isLive) 0.5f else 0.7f
                    )
                }
                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isLive) Color(0xFFFFEBEE) else Color(0xFFE6F0FF))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isLive) "EN VIVO" else "PRÓXIMO",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isLive) Color(0xFFE53935) else Color(0xFF2F6FED),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = timeRange, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = event.ubicacion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onDetail,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Ver detalles", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAgendaScreenPreview() {
    IntegradoraEventNodeTheme { AdminAgendaScreen() }
}
