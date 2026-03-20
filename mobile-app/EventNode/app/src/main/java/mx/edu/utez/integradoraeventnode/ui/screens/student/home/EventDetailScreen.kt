package mx.edu.utez.integradoraeventnode.ui.screens.student.home

import android.graphics.BitmapFactory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.student.profile.ProfileBottomNav
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EventDetailScreen(
    eventId: Int = -1,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
    val userId = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""
    val bearerToken = if (token.isNotEmpty()) "Bearer $token" else ""

    var showRegisterModal by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var evento by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isRegistering by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        if (eventId == -1) {
            errorMessage = "ID de evento no válido"
            isLoading = false
            return@LaunchedEffect
        }
        scope.launch {
            try {
                isLoading = true
                val response = ApiClient.apiService.getEvento(eventId)
                if (response.isSuccessful) {
                    val eventoData = response.body()
                    if (eventoData != null) {
                        evento = mapOf(
                            "nombre" to eventoData.nombre,
                            "nombreCategoria" to eventoData.nombreCategoria,
                            "ubicacion" to eventoData.ubicacion,
                            "descripcion" to eventoData.descripcion,
                            "banner" to eventoData.banner,
                            "fechaInicio" to eventoData.fechaInicio,
                            "fechaFin" to eventoData.fechaFin
                        )
                    }
                }
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar evento: ${e.message}"
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
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
            } else if (errorMessage != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage ?: "Desconocido", textAlign = TextAlign.Center)
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
                    .padding(bottom = 90.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val bannerBitmap = (evento?.get("banner") as? String)?.let { base64String ->
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
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            bitmap = assetImageBitmap("Gemini_Generated_Image_j7p5usj7p5usj7p5.png"),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .align(Alignment.TopStart)
                    ) {
                        Image(
                            bitmap = assetImageBitmap("arrow-small-left.png"),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 32.dp, bottomEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = evento?.get("nombre") as? String ?: "Evento",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF0F7FF),
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text(
                                    text = (evento?.get("nombreCategoria") as? String ?: "EVENTO").uppercase(),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2F6FED)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        val startDate = (evento?.get("fechaInicio") as? String)?.let { formatDateTime(it) } ?: "N/A"
                        val endDate = (evento?.get("fechaFin") as? String)?.let { formatDateTime(it) } ?: "N/A"

                        DetailRow(
                            label = "FECHA Y HORA",
                            value = "$startDate - $endDate",
                            icon = "book-open-reader.png"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        DetailRow(
                            label = "UBICACIÓN",
                            value = evento?.get("ubicacion") as? String ?: "N/A",
                            icon = "home.png"
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Información del Evento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = evento?.get("descripcion") as? String ?: "Sin descripción",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF44474E),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = {
                                isRegistering = true
                                scope.launch {
                                    try {
                                        if (userId != -1 && bearerToken.isNotEmpty()) {
                                            val response = ApiClient.apiService.inscribirse(
                                                bearerToken,
                                                mapOf("idUsuario" to userId, "idEvento" to eventId)
                                            )
                                            if (response.isSuccessful) {
                                                showRegisterModal = true
                                            }
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error al registrarse: ${e.message}"
                                    } finally {
                                        isRegistering = false
                                    }
                                }
                            },
                            enabled = !isRegistering,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                        ) {
                            Text("Registrarse al Evento", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                ProfileBottomNav(onHome = onBack, onAgenda = onAgenda, onDiplomas = onDiplomas, onProfile = onProfile)
            }

            if (showRegisterModal) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .widthIn(max = 360.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Illustration Placeholder
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFDF2E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("Gemini_Generated_Image_82kh6j82kh6j82kh.png"),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "¡Registro exitoso!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Todo listo para asistir al '${evento?.get("nombre") ?: "evento"}'. Prepárate para una experiencia inspiradora.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Button(
                                onClick = { 
                                    showRegisterModal = false 
                                    onAgenda()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                            ) {
                                Text("Ver en agenda", fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Button(
                                onClick = { showRegisterModal = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA)),
                                border = BorderStroke(1.dp, Color(0xFFE1E2EC))
                            ) {
                                Text("Volver a descubrir", color = Color(0xFF44474E), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDateTime(dateTimeString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM - HH:mm", Locale("es", "ES"))
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        dateTimeString
    }
}

@Composable
private fun DetailRow(label: String, value: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0F7FF),
            border = BorderStroke(1.dp, Color(0xFFE1E2EC))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    bitmap = assetImageBitmap(icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label, 
                style = MaterialTheme.typography.labelSmall, 
                fontWeight = FontWeight.Bold,
                color = Color(0xFF74777F),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value, 
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C1E)
            )
        }
    }
}

@Composable
private fun DetailBottomNav(onBack: () -> Unit, onAgenda: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(label = "Inicio", icon = "home.png", selected = true, onClick = onBack)
            BottomNavItem(label = "Agenda", icon = "book-open-reader.png", selected = false, onClick = onAgenda)
            BottomNavItem(label = "Diplomas", icon = "diploma.png", selected = false, onClick = onDiplomas)
            BottomNavItem(label = "Perfil", icon = "user.png", selected = false, onClick = onProfile)
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            bitmap = assetImageBitmap(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailScreenPreview() {
    IntegradoraEventNodeTheme {
        EventDetailScreen()
    }
}
