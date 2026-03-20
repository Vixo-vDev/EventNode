package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.min
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
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
    var selectedTab by remember { mutableStateOf(0) } // 0: Próximos, 1: Pasados
    var isLoading by remember { mutableStateOf(true) }
    var allEvents by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }
    var upcomingGrouped by remember { mutableStateOf<List<Pair<String, List<EventoResponse>>>>(emptyList()) }
    var pastEvents by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""
                val bearerToken = "Bearer $token"

                if (token.isNotEmpty()) {
                    val response = ApiClient.apiService.getEventosFiltrados(bearerToken)
                    if (response.isSuccessful) {
                        allEvents = response.body() ?: emptyList()

                        // Split into upcoming and past
                        val now = LocalDateTime.now()
                        val upcoming = allEvents.filter { event ->
                            try {
                                val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                LocalDateTime.parse(event.fechaInicio, formatter).isAfter(now)
                            } catch (e: Exception) {
                                false
                            }
                        }
                        pastEvents = allEvents.filter { event ->
                            try {
                                val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                LocalDateTime.parse(event.fechaInicio, formatter).isBefore(now)
                            } catch (e: Exception) {
                                false
                            }
                        }

                        // Group upcoming by date
                        upcomingGrouped = groupEventsByDate(upcoming)
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp)
                ) {
                    // Tabs
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TabItem(
                                text = "Próximos",
                                selected = selectedTab == 0,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedTab = 0 }
                            )
                            TabItem(
                                text = "Pasados",
                                selected = selectedTab == 1,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedTab = 1 }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp)
                    ) {
                        if (selectedTab == 0) {
                            if (upcomingGrouped.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No hay eventos próximos", color = Color.Gray)
                                }
                            } else {
                                upcomingGrouped.forEach { (dateLabel, events) ->
                                    Text(
                                        text = dateLabel,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )

                                    events.forEach { event ->
                                        AdminAgendaCard(
                                            event = event,
                                            onDetail = { onViewDetail(event.idEvento) }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        } else {
                            if (pastEvents.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No hay eventos pasados", color = Color.Gray)
                                }
                            } else {
                                pastEvents.forEach { event ->
                                    CompactEventCard(
                                        event = event,
                                        onViewDetail = { onViewDetail(event.idEvento) }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
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

private fun groupEventsByDate(events: List<EventoResponse>): List<Pair<String, List<EventoResponse>>> {
    val grouped = mutableMapOf<String, MutableList<EventoResponse>>()
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("es", "ES"))

    events.forEach { event ->
        try {
            val eventDate = LocalDateTime.parse(event.fechaInicio, formatter).toLocalDate()
            val dateLabel = when {
                eventDate == today -> "Hoy, ${today.format(dateFormatter)}"
                eventDate == today.plusDays(1) -> "Mañana, ${today.plusDays(1).format(dateFormatter)}"
                else -> eventDate.format(dateFormatter)
            }

            grouped.getOrPut(dateLabel) { mutableListOf() }.add(event)
        } catch (e: Exception) {
            // Skip events that can't be parsed
        }
    }

    return grouped.toList().sortedBy { it.first }
}

@Composable
private fun TabItem(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFF2F6FED) else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(2.dp)
                .background(if (selected) Color(0xFF2F6FED) else Color.Transparent)
        )
    }
}

@Composable
private fun AdminAgendaCard(
    event: EventoResponse,
    onDetail: () -> Unit
) {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val (hours, minutes) = try {
        val startTime = LocalDateTime.parse(event.fechaInicio, formatter)
        val endTime = LocalDateTime.parse(event.fechaFin, formatter)
        val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val endStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        Pair(startStr, endStr)
    } catch (e: Exception) {
        Pair("", "")
    }

    val timeRange = if (hours.isNotEmpty() && minutes.isNotEmpty()) "$hours - $minutes" else "Hora no disponible"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                // Banner image or placeholder - decode outside composable tree
                val bannerBitmap = remember(event.banner) {
                    if (!event.banner.isNullOrEmpty()) {
                        try {
                            val decodedBytes = android.util.Base64.decode(event.banner!!, android.util.Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
                        } catch (e: Exception) {
                            null
                        }
                    } else null
                }

                if (bannerBitmap != null) {
                    Image(
                        bitmap = bannerBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFF2F6FED))
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕒", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = timeRange, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = event.ubicacion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDetail,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Ver detalles")
                }
            }
        }
    }
}

@Composable
private fun CompactEventCard(
    event: EventoResponse,
    onViewDetail: () -> Unit
) {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val (day, month) = try {
        val eventDate = LocalDateTime.parse(event.fechaInicio, formatter)
        val dayStr = eventDate.dayOfMonth.toString().padStart(2, '0')
        val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale("es", "ES"))
        val monthStr = eventDate.format(monthFormatter).uppercase()
        Pair(dayStr, monthStr)
    } catch (e: Exception) {
        Pair("--", "---")
    }

    val (hours, minutes) = try {
        val startTime = LocalDateTime.parse(event.fechaInicio, formatter)
        val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        Pair(startStr, "")
    } catch (e: Exception) {
        Pair("", "")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onViewDetail() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F6FA)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = day, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2F6FED))
                Text(text = month, fontSize = 12.sp, color = Color(0xFF2F6FED))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.nombre, fontWeight = FontWeight.Bold)
                Text(text = "${hours} - ${event.ubicacion}", fontSize = 12.sp, color = Color.Gray)
                Text(text = event.estado, fontSize = 11.sp, color = Color.LightGray)
            }

            TextButton(onClick = onViewDetail) {
                Text("Ver detalles", fontSize = 12.sp, color = Color(0xFF2F6FED))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAgendaScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminAgendaScreen()
    }
}