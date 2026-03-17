package mx.edu.utez.integradoraeventnode.ui.screens.student.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.util.Base64
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.student.profile.ProfileBottomNav
import androidx.compose.ui.graphics.asImageBitmap


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onViewDetails: (Int) -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    // Helper function to decode base64 images
    fun decodeBase64Image(base64Str: String?): ImageBitmap? {
        if (base64Str.isNullOrEmpty()) return null
        return try {
            val cleanBase64 = if (base64Str.contains(",")) {
                base64Str.substringAfter(",")
            } else {
                base64Str
            }
            val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    var searchText by remember { mutableStateOf("") }
    
    var eventos by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = ApiClient.apiService.getEventos()
            if (response.isSuccessful) {
                eventos = response.body() ?: emptyList()
            } else {
                errorMessage = "Error al cargar eventos"
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión"
        } finally {
            isLoading = false
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar eventos por nombre...", color = Color(0xFF999999)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    leadingIcon = {
                        Image(
                            bitmap = assetImageBitmap("home.png"), // Reusing home as placeholder for search
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(title = "Próximos Eventos", action = "Ver más eventos")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isLoading) {
                        Text(
                            text = "Cargando eventos...", 
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            textAlign = TextAlign.Center
                        )
                    } else if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            textAlign = TextAlign.Center
                        )
                    } else if (eventos.isEmpty()) {
                        Text(
                            text = "No hay eventos próximos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        eventos.forEach { evento ->
                            val cat = evento.nombreCategoria ?: "EVENTO"
                            val mainTextLength = if (evento.nombre.length > 15) 15 else evento.nombre.length
                            val main = evento.nombre.substring(0, mainTextLength).uppercase()
                            
                            val dateStr = if (evento.fechaInicio.length >= 16) {
                                evento.fechaInicio.substring(0, 10).replace("-", "/") + " • " + evento.fechaInicio.substring(11, 16)
                            } else {
                                evento.fechaInicio
                            }

                            EventCard(
                                tag = evento.estado.uppercase(),
                                category = cat,
                                mainText = main,
                                title = evento.nombre,
                                date = dateStr,
                                location = evento.ubicacion,
                                buttonText = "Ver Detalles",
                                accent = Color(0xFF6F9EA6),
                                bannerBase64 = evento.banner,
                                bannerDecoder = ::decodeBase64Image,
                                onDetailsClick = { onViewDetails(evento.idEvento) }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    SectionHeader(title = "Diploma", action = "Ver historial", onActionClick = onDiplomas)
                    Spacer(modifier = Modifier.height(12.dp))
                    SimpleEventCard(
                        category = "WEB DEV",
                        mainText = "SUMMIT",
                        title = "Web Development Summit '23",
                        subtitle = "Septiembre 2023",
                        status = "DIPLOMA EMITIDO",
                        cardColor = Color(0xFFC9D7C4),
                        onClick = onDiplomas
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SimpleEventCard(
                        category = "MINIMAL",
                        mainText = "DATA SCIENCE",
                        title = "Seminario Avanzado: Big Data",
                        subtitle = "Agosto 2023",
                        status = "DIPLOMA EMITIDO",
                        cardColor = Color(0xFFA8B8B6),
                        onClick = onDiplomas
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ProfileBottomNav(
                    currentScreen = "Inicio",
                    onHome = {},
                    onAgenda = onAgenda,
                    onDiplomas = onDiplomas,
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String, onActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2F6FED))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Text(
            text = action,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF2F6FED),
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
private fun EventCard(
    tag: String,
    category: String,
    mainText: String,
    title: String,
    date: String,
    location: String,
    buttonText: String,
    accent: Color,
    bannerBase64: String? = null,
    bannerDecoder: (String?) -> ImageBitmap? = { null },
    onDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(accent)
            ) {
                // Background Image
                val decodedBanner = bannerDecoder(bannerBase64)
                if (decodedBanner != null) {
                    Image(
                        bitmap = decodedBanner,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.6f
                    )
                } else {
                    Image(
                        bitmap = assetImageBitmap("Gemini_Generated_Image_j7p5usj7p5usj7p5.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.6f
                    )
                }
                
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = tag,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if(tag == "ACTIVO") Color(0xFF2F6FED) else Color(0xFF757575))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = mainText,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 4.sp
                        )
                    }
                    
                    // Spacer for layout balance
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        bitmap = assetImageBitmap("home.png"),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = date, style = MaterialTheme.typography.bodySmall, color = Color(0xFF74777F))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        bitmap = assetImageBitmap("home.png"), // Using home as placeholder for location
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = location, style = MaterialTheme.typography.bodySmall, color = Color(0xFF74777F))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDetailsClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text(buttonText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SimpleEventCard(
    category: String,
    mainText: String,
    title: String,
    subtitle: String,
    status: String,
    cardColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(cardColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = mainText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle, 
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF74777F)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            bitmap = assetImageBitmap("check-circle.png"),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = status, 
                            style = MaterialTheme.typography.labelSmall, 
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Text(
                        text = "Ver más",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2F6FED),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IntegradoraEventNodeTheme {
        HomeScreen()
    }
}
