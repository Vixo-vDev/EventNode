package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AdminEventDetailScreen(
    modifier: Modifier = Modifier,
    eventId: Int = 1,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {},
    onEditEvent: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showAttendeesList by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var eventData by remember { mutableStateOf<mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse?>(null) }
    var inscritos by remember { mutableStateOf(0) }
    var asistencias by remember { mutableStateOf(0) }
    var attendeesList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(eventId) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = ""

                val sharedPref = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("token", "") ?: ""
                val bearerToken = "Bearer $token"

                // Fetch event details
                val eventResponse = ApiClient.apiService.getEvento(eventId)
                if (eventResponse.isSuccessful) {
                    eventData = eventResponse.body()
                }

                // Fetch registered count
                val inscrptosResponse = ApiClient.apiService.contarInscritos(bearerToken, eventId)
                if (inscrptosResponse.isSuccessful) {
                    inscritos = (inscrptosResponse.body()?.get("count") as? Number)?.toInt() ?: 0
                }

                // Fetch attendance count
                val asistenciasResponse = ApiClient.apiService.contarAsistencias(bearerToken, eventId)
                if (asistenciasResponse.isSuccessful) {
                    asistencias = (asistenciasResponse.body()?.get("count") as? Number)?.toInt() ?: 0
                }

                // Fetch attendees list (only loaded when dialog opens)
                val asistenciasListResponse = ApiClient.apiService.listarAsistencias(bearerToken, eventId)
                if (asistenciasListResponse.isSuccessful) {
                    attendeesList = asistenciasListResponse.body() ?: emptyList()
                }

                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar el evento: ${e.message}"
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF2F6FED),
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando evento...")
                }
            } else if (errorMessage.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text("Volver")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp)
                ) {
                    // Header with Image
                    Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                        val bannerBitmap = eventData?.banner?.let { base64String ->
                            try {
                                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
                            } catch (e: Exception) {
                                null
                            }
                        }

                        if (bannerBitmap != null) {
                            Image(
                                bitmap = bannerBitmap,
                                contentDescription = "Imagen del evento",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                bitmap = assetImageBitmap("Gemini_Generated_Image_j7p5usj7p5usj7p5.png"),
                                contentDescription = "Imagen del evento",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    
                    // Back button (Moved to LEFT as requested)
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                    ) {
                        Image(
                            bitmap = assetImageBitmap("arrow-small-left.png"),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // View-only mode — CRUD is handled on web portal
                    }

                    // Overlapping Card (Design from Image 1)
                    Column(
                        modifier = Modifier
                            .offset(y = (-40).dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = eventData?.nombre ?: "Evento",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 28.sp
                                    )
                                    Surface(
                                        color = Color(0xFFE3F2FD),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = eventData?.categoriaNombre?.uppercase() ?: "EVENTO",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = Color(0xFF2196F3),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                val startDate = eventData?.fechaInicio?.let { formatDateTime(it) } ?: "N/A"
                                val endDate = eventData?.fechaFin?.let { formatDateTime(it) } ?: "N/A"

                                EventInfoItem(
                                    icon = "book-open-reader.png",
                                    title = "FECHA Y HORA",
                                    subtitle = "$startDate - $endDate"
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                EventInfoItem(
                                    icon = "home.png",
                                    title = "UBICACIÓN",
                                    subtitle = eventData?.ubicacion ?: "N/A"
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                EventInfoItem(
                                    icon = "user.png",
                                    title = "CAPACIDAD",
                                    subtitle = "${eventData?.capacidadMaxima ?: 0} Personas"
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Información del Evento",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = eventData?.descripcion ?: "Sin descripción",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Estado: ${eventData?.estado ?: "N/A"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2F6FED),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Admin Management Section (Design from Image 2)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "MÉTRICAS DE ASISTENCIA",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val capacidad = eventData?.capacidadMaxima ?: 1
                        val progressRegistrados = if (capacidad > 0) inscritos.toFloat() / capacidad else 0f

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            MetricCard(
                                title = "REGISTRADOS",
                                count = inscritos.toString(),
                                total = capacidad.toString(),
                                modifier = Modifier.weight(1f),
                                progress = progressRegistrados.coerceIn(0f, 1f),
                                progressColor = Color(0xFF2F6FED)
                            )
                            MetricCard(
                                title = "ASISTIERON",
                                count = asistencias.toString(),
                                total = "en vivo",
                                modifier = Modifier.weight(1f),
                                showTrend = true,
                                progressColor = Color(0xFF4CAF50)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "ACCIONES RÁPIDAS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { showAttendeesList = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F6FA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = assetImageBitmap("user.png"),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Ver Lista de Asistencia", fontWeight = FontWeight.Bold)
                                    Text(text = "Gestionar participantes y registros", fontSize = 12.sp, color = Color.Gray)
                                }
                                Text("〉", color = Color.LightGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Event status badge (view only — CRUD is web-only)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = when (eventData?.estado) {
                                "ACTIVO" -> Color(0xFFE8F5E9)
                                "CANCELADO" -> Color(0xFFFFEBEE)
                                "FINALIZADO" -> Color(0xFFF5F5F5)
                                else -> Color(0xFFF5F5F5)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Estado: ${eventData?.estado ?: "N/A"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = when (eventData?.estado) {
                                        "ACTIVO" -> Color(0xFF4CAF50)
                                        "CANCELADO" -> Color(0xFFD32F2F)
                                        "FINALIZADO" -> Color.Gray
                                        else -> Color.Gray
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Nav
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AdminBottomNav(
                    selected = "Agenda",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }

    if (showAttendeesList) {
        AttendeesListDialog(
            attendeesList = attendeesList,
            onDismiss = { showAttendeesList = false }
        )
    }
}

private fun formatDateTime(isoString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy • HH:mm", Locale("es", "ES"))
        dateTime.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}

@Composable
private fun EventInfoItem(icon: String, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F7FF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = assetImageBitmap(icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
            Text(text = subtitle, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        }
    }
}

@Composable
private fun AttendeesListDialog(
    attendeesList: List<Map<String, Any>>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Lista de Asistencia", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (attendeesList.isEmpty()) {
                        Text(text = "No hay asistencias registradas", color = Color.Gray)
                    } else {
                        attendeesList.forEachIndexed { index, attendee ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF5F6FA)), contentAlignment = Alignment.Center) {
                                    Text(text = "${index + 1}", fontWeight = FontWeight.Bold, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    val nombre = (attendee["nombre"] ?: attendee["alumno"] ?: "Estudiante").toString()
                                    val matricula = (attendee["matricula"] ?: attendee["id"] ?: "N/A").toString()
                                    Text(text = nombre, fontWeight = FontWeight.Bold)
                                    Text(text = matricula, fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                            HorizontalDivider(color = Color(0xFFF5F6FA))
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    count: String,
    total: String,
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressColor: Color = Color(0xFF2F6FED),
    showTrend: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = count, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "/ $total", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (showTrend) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("↗", color = Color(0xFF4CAF50), fontSize = 12.sp)
                    Text("Sincronizado", color = Color(0xFF4CAF50), fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp))
                }
            } else {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = progressColor,
                    trackColor = progressColor.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminEventDetailScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminEventDetailScreen(eventId = 1)
    }
}