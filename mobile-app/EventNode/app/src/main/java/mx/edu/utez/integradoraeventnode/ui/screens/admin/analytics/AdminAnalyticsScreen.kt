package mx.edu.utez.integradoraeventnode.ui.screens.admin.analytics

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme

data class EventAttendanceData(
    val eventName: String,
    val attendance: Float
)

data class CategoryData(
    val categoryName: String,
    val eventCount: Int,
    val totalEvents: Int
)

@Composable
fun AdminAnalyticsScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var totalEvents by remember { mutableStateOf(0) }
    var activeEvents by remember { mutableStateOf(0) }
    var totalDiplomasEmitted by remember { mutableStateOf(0) }
    var totalDiplomasPending by remember { mutableStateOf(0) }
    var overallAttendanceRate by remember { mutableStateOf(0f) }
    var eventAttendanceList by remember { mutableStateOf<List<EventAttendanceData>>(emptyList()) }
    var categoryDataList by remember { mutableStateOf<List<CategoryData>>(emptyList()) }
    var malePercentage by remember { mutableStateOf(0f) }
    var femalePercentage by remember { mutableStateOf(0f) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""

                if (token.isEmpty()) {
                    errorMessage = "Token no disponible"
                    isLoading = false
                    return@launch
                }

                // Fetch events
                val eventsResponse = ApiClient.apiService.getEventosFiltrados("Bearer $token")
                if (eventsResponse.isSuccessful) {
                    val events = eventsResponse.body() ?: emptyList()
                    totalEvents = events.size
                    activeEvents = events.count { it.estado.equals("ACTIVO", ignoreCase = true) }

                    // Calculate attendance and build event list
                    var totalAsistencias = 0
                    var totalInscritos = 0
                    val attendanceList = mutableListOf<EventAttendanceData>()

                    events.forEach { event ->
                        val eventId = event.idEvento
                        val eventName = event.nombre

                        try {
                            val asistenciaResponse = ApiClient.apiService.contarAsistencias("Bearer $token", eventId)
                            val inscrResponse = ApiClient.apiService.contarInscritos("Bearer $token", eventId)

                            val asistencias = if (asistenciaResponse.isSuccessful) {
                                (asistenciaResponse.body()?.get("totalAsistencias") as? Number)?.toInt() ?: 0
                            } else 0

                            val inscritos = if (inscrResponse.isSuccessful) {
                                (inscrResponse.body()?.get("totalInscritos") as? Number)?.toInt() ?: 0
                            } else 0

                            totalAsistencias += asistencias
                            totalInscritos += inscritos

                            if (inscritos > 0) {
                                val attendanceRate = (asistencias.toFloat() / inscritos.toFloat()).coerceIn(0f, 1f)
                                attendanceList.add(EventAttendanceData(eventName, attendanceRate))
                            }
                        } catch (e: Exception) {
                            // Skip this event on error
                        }
                    }

                    // Calculate overall attendance rate
                    overallAttendanceRate = if (totalInscritos > 0) {
                        (totalAsistencias.toFloat() / totalInscritos.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    // Take top 7 events
                    eventAttendanceList = attendanceList
                        .sortedByDescending { it.attendance }
                        .take(7)
                }

                // Fetch diplomas
                val diplomasResponse = ApiClient.apiService.listarDiplomas("Bearer $token")
                if (diplomasResponse.isSuccessful) {
                    val diplomas = diplomasResponse.body() ?: emptyList()
                    totalDiplomasEmitted = diplomas.sumOf { (it["totalEmitidos"] as? Number)?.toInt() ?: 0 }
                    totalDiplomasPending = diplomas.sumOf { (it["totalPendientes"] as? Number)?.toInt() ?: 0 }
                }

                // Count events by category using EventoResponse.nombreCategoria
                val categoryMap = mutableMapOf<String, Int>()
                eventsResponse.body()?.forEach { event ->
                    val categoryName = event.nombreCategoria ?: "Sin categoría"
                    categoryMap[categoryName] = (categoryMap[categoryName] ?: 0) + 1
                }
                val totalEventsCount = totalEvents
                categoryDataList = categoryMap.map { (name, count) ->
                    CategoryData(name, count, totalEventsCount)
                }.sortedByDescending { it.eventCount }

                // Fetch users and calculate demographics
                val usuariosResponse = ApiClient.apiService.listarUsuarios("Bearer $token")
                if (usuariosResponse.isSuccessful) {
                    val usuarios = usuariosResponse.body() ?: emptyList()
                    var maleCount = 0
                    var femaleCount = 0

                    usuarios.forEach { user ->
                        val sexo = user["sexo"] as? String ?: ""
                        when (sexo.uppercase()) {
                            "M" -> maleCount++
                            "F" -> femaleCount++
                        }
                    }

                    val totalUsers = maleCount + femaleCount
                    if (totalUsers > 0) {
                        malePercentage = (maleCount.toFloat() / totalUsers.toFloat()).coerceIn(0f, 1f)
                        femalePercentage = (femaleCount.toFloat() / totalUsers.toFloat()).coerceIn(0f, 1f)
                    }
                }

                isLoading = false
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2F6FED))
                    }
                } else if (errorMessage.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error", color = Color.Red, fontWeight = FontWeight.Bold)
                        Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
                    }
                } else {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Summary Stats Section
                        Text(
                            text = "Resumen de eventos",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard("Total de Eventos", totalEvents.toString(), Color(0xFF2F6FED))
                            StatCard(
                                "Eventos Activos",
                                if (activeEvents > 0) "LIVE $activeEvents" else "$activeEvents",
                                if (activeEvents > 0) Color(0xFFFF6B6B) else Color(0xFF2F6FED)
                            )
                            StatCard(
                                "Diplomas",
                                "$totalDiplomasEmitted/${totalDiplomasEmitted + totalDiplomasPending}",
                                Color(0xFF4CAF50)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Attendance Rate Section
                        Text(
                            text = "Rendimiento del evento",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = "Tasa de asistencia promedio", fontSize = 12.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${(overallAttendanceRate * 100).toInt()}%",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFE8F5E9))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Datos en vivo",
                                            color = Color(0xFF4CAF50),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                // Event attendance bar chart
                                if (eventAttendanceList.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        eventAttendanceList.forEachIndexed { index, data ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight(data.attendance)
                                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                    .background(if (index == eventAttendanceList.lastIndex) Color(0xFF2F6FED) else Color(0xFFBBDEFB))
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Categories Section
                        Text(
                            text = "Categorías de eventos populares",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                if (categoryDataList.isEmpty()) {
                                    Text("No hay categorías disponibles", fontSize = 12.sp, color = Color.Gray)
                                } else {
                                    categoryDataList.take(3).forEach { category ->
                                        val percentage = if (category.totalEvents > 0) {
                                            category.eventCount.toFloat() / category.totalEvents.toFloat()
                                        } else 0f
                                        val percentageText = "${(percentage * 100).toInt()}%"
                                        CategoryProgressItem(
                                            category.categoryName,
                                            percentage,
                                            percentageText,
                                            Color(0xFF2F6FED)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Demographics Section
                        Text(
                            text = "Desglose demográfico",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = "Usuarios por género", fontSize = 12.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(24.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        DemographicBar(
                                            "${(malePercentage * 100).toInt()}%",
                                            "HOMBRES",
                                            Color(0xFFE3F2FD)
                                        )
                                        DemographicBar(
                                            "${(femalePercentage * 100).toInt()}%",
                                            "MUJERES",
                                            Color(0xFF2F6FED)
                                        )
                                    }
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
                    selected = "Analitica",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = {},
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
private fun CategoryProgressItem(label: String, progress: Float, value: String, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (value.contains("+")) Color(0xFF2F6FED) else Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF5F6FA)
        )
    }
}

@Composable
private fun DemographicBar(percentage: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = percentage,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (color == Color(0xFF2F6FED)) Color.White else Color(0xFF2F6FED)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAnalyticsScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminAnalyticsScreen()
    }
}