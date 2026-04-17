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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import android.util.Base64
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: agenda del alumno con eventos activos/proximos y acceso a QR.
 * Por que existe: sincroniza precheckin y asistencia del backend con estado visual de la app.
 */
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onViewQr: (Int, String) -> Unit = { _, _ -> },
    onViewDetail: (Int) -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var allEnrolledEvents by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // eventId -> true si la asistencia ya fue registrada
    var attendanceStatus by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE)
    val usuarioId = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""
    val bearerToken = if (token.isNotEmpty()) "Bearer $token" else ""

    fun decodeBase64Image(base64Str: String?): ImageBitmap? {
        if (base64Str.isNullOrEmpty()) return null
        return try {
            val clean = if (base64Str.contains(",")) base64Str.substringAfter(",") else base64Str
            val bytes = Base64.decode(clean, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) { null }
    }

    fun parseDateTime(str: String?): LocalDateTime? {
        if (str.isNullOrEmpty()) return null
        return try {
            LocalDateTime.parse(str.take(19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: Exception) { null }
    }

    LaunchedEffect(Unit) {
        if (usuarioId != -1 && bearerToken.isNotEmpty()) {
            try {
                val response = ApiClient.apiService.listarMisEventos(bearerToken, usuarioId)
                if (response.isSuccessful) {
                    allEnrolledEvents = (response.body() ?: emptyList()).filter {
                        it["inscripcionEstado"] == "ACTIVO" &&
                                it["eventoEstado"] != "CANCELADO"
                    }
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

    // Verifica asistencia para eventos EN VIVO cuando se carguen los eventos
    LaunchedEffect(allEnrolledEvents) {
        if (allEnrolledEvents.isEmpty() || bearerToken.isEmpty()) return@LaunchedEffect
        val now = LocalDateTime.now()
        val liveIds = allEnrolledEvents
            .filter { ev ->
                val inicio = parseDateTime(ev["fechaInicio"] as? String)
                val fin = parseDateTime(ev["fechaFin"] as? String)
                inicio != null && fin != null && !inicio.isAfter(now) && !fin.isBefore(now)
            }
            .mapNotNull { ev ->
                (ev["idEvento"] as? Double)?.toInt() ?: (ev["idEvento"] as? Int)
            }

        if (liveIds.isEmpty()) return@LaunchedEffect

        val statusMap = mutableMapOf<Int, Boolean>()
        liveIds.forEach { eventId ->
            try {
                val resp = ApiClient.apiService.listarAsistencias(bearerToken, eventId)
                if (resp.isSuccessful) {
                    statusMap[eventId] = (resp.body() ?: emptyList()).any { entry ->
                        val entryUserId = (entry["idUsuario"] as? Number)?.toInt()
                            ?: (entry["alumnoId"] as? Number)?.toInt()
                            ?: (entry["idAlumno"] as? Number)?.toInt()
                        entryUserId == usuarioId
                    }
                } else {
                    statusMap[eventId] = false
                }
            } catch (e: Exception) {
                statusMap[eventId] = false
            }
        }
        attendanceStatus = statusMap
    }

    val now = LocalDateTime.now()

    // EN VIVO: fechaInicio <= now <= fechaFin
    val enVivoEvents = allEnrolledEvents.filter { ev ->
        val inicio = parseDateTime(ev["fechaInicio"] as? String)
        val fin = parseDateTime(ev["fechaFin"] as? String)
        inicio != null && fin != null && !inicio.isAfter(now) && !fin.isBefore(now)
    }

    // PRÓXIMOS: fechaInicio > now y no FINALIZADO
    val proximosEvents = allEnrolledEvents.filter { ev ->
        val inicio = parseDateTime(ev["fechaInicio"] as? String)
        val estado = ev["eventoEstado"] as? String ?: ""
        inicio != null && inicio.isAfter(now) && estado != "FINALIZADO"
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
                    text = "Mis Eventos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) { Text("Cargando tu agenda...", color = Color.Gray) }
                    }
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(errorMessage!!, color = MaterialTheme.colorScheme.error) }
                    }
                    enVivoEvents.isEmpty() && proximosEvents.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No tienes eventos activos ni próximos.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        // ── Sección EN VIVO ──
                        if (enVivoEvents.isNotEmpty()) {
                            SectionLabel(label = "ACTIVO", color = Color(0xFF2F6FED))
                            Spacer(modifier = Modifier.height(8.dp))
                            enVivoEvents.forEach { ev ->
                                val evId = (ev["idEvento"] as? Double)?.toInt()
                                    ?: (ev["idEvento"] as? Int) ?: -1
                                AgendaCard(
                                    ev = ev,
                                    isLive = true,
                                    hasAttendance = attendanceStatus[evId] ?: false,
                                    bannerDecoder = ::decodeBase64Image,
                                    onViewQr = onViewQr,
                                    onViewDetail = onViewDetail
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // ── Sección PRÓXIMOS ──
                        if (proximosEvents.isNotEmpty()) {
                            SectionLabel(label = "PRÓXIMOS", color = Color(0xFF757575))
                            Spacer(modifier = Modifier.height(8.dp))
                            proximosEvents.forEach { ev ->
                                AgendaCard(
                                    ev = ev,
                                    isLive = false,
                                    hasAttendance = false,
                                    bannerDecoder = ::decodeBase64Image,
                                    onViewQr = onViewQr,
                                    onViewDetail = onViewDetail
                                )
                                Spacer(modifier = Modifier.height(12.dp))
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
                AgendaBottomNav(onHome = onHome, onDiplomas = onDiplomas, onProfile = onProfile)
            }
        }
    }
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
private fun AgendaCard(
    ev: Map<String, Any>,
    isLive: Boolean,
    hasAttendance: Boolean,
    bannerDecoder: (String?) -> ImageBitmap?,
    onViewQr: (Int, String) -> Unit,
    onViewDetail: (Int) -> Unit
) {
    val eventId = (ev["idEvento"] as? Double)?.toInt() ?: (ev["idEvento"] as? Int) ?: -1
    val nombre = ev["nombre"] as? String ?: "Evento"
    val fechaInicio = ev["fechaInicio"] as? String ?: ""
    val ubicacion = ev["ubicacion"] as? String ?: "Sin ubicación"
    val bannerBase64 = ev["banner"] as? String
    val toleranciaMinutos = (ev["tiempoToleranciaMinutos"] as? Number)?.toInt() ?: 0

    val dateStr = if (fechaInicio.length >= 16) {
        fechaInicio.substring(0, 10).replace("-", "/") + " • " + fechaInicio.substring(11, 16)
    } else fechaInicio

    val now = LocalDateTime.now()
    val inicioDateTime = try {
        LocalDateTime.parse(fechaInicio.take(19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
    } catch (e: Exception) { null }

    // Retardo: solo aplica si NO hay asistencia registrada y se pasó la tolerancia
    val isPastTolerance = isLive && !hasAttendance && inicioDateTime != null &&
            now.isAfter(inicioDateTime.plusMinutes(toleranciaMinutos.toLong()))

    // QR habilitado si el evento está EN VIVO y no hay asistencia aún
    val qrEnabled = isLive && !hasAttendance

    // Mismo tratamiento que Inicio (EventCard): acento neutro, sin capa roja sobre el banner
    val bannerAccent = Color(0xFF6F9EA6)
    val bannerImageAlpha = 0.6f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .clickable { onViewDetail(eventId) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(bannerAccent)
            ) {
                val decoded = bannerDecoder(bannerBase64)
                if (decoded != null) {
                    Image(
                        bitmap = decoded,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = bannerImageAlpha
                    )
                } else {
                    Image(
                        bitmap = assetImageBitmap("Gemini_Generated_Image_j7p5usj7p5usj7p5.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = bannerImageAlpha
                    )
                }
                // Misma lógica visual que Inicio (EventCard): ACTIVO azul, PRÓXIMO gris — texto en blanco
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isLive) Color(0xFF2F6FED) else Color(0xFF757575))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isLive) "ACTIVO" else "PRÓXIMO",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1C1E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = dateStr, style = MaterialTheme.typography.bodySmall, color = Color(0xFF74777F))
                Text(text = ubicacion, style = MaterialTheme.typography.bodySmall, color = Color(0xFF74777F))
                Spacer(modifier = Modifier.height(10.dp))

                when {
                    // ── Ya registrado ──
                    hasAttendance -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF4CAF50))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Asistencia registrada",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "Tu asistencia a este evento ya fue confirmada.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }
                    }

                    // ── Retardo: no registrado y fuera de tolerancia ──
                    isPastTolerance -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF3E0))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF57C00))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "El tiempo de tolerancia ha expirado. Tu asistencia se registrará como retardo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onViewQr(eventId, nombre) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00))
                        ) {
                            Image(
                                bitmap = assetImageBitmap("qr-scan.png"),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver QR (Retardo)")
                        }
                    }

                    // ── En vivo, dentro de tolerancia ──
                    isLive -> {
                        Button(
                            onClick = { onViewQr(eventId, nombre) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
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

                    // ── Próximo: no ha iniciado ──
                    else -> {
                        Button(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color(0xFFE0E0E0),
                                disabledContentColor = Color(0xFF9E9E9E)
                            )
                        ) {
                            Text("Disponible al iniciar el evento")
                        }
                    }
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
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
            colorFilter = ColorFilter.tint(color)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaScreenPreview() {
    IntegradoraEventNodeTheme { AgendaScreen() }
}
