package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AdminEditEventScreen(
    modifier: Modifier = Modifier,
    eventId: Int = 1,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(eventId) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = ""

                val eventResponse = ApiClient.apiService.getEvento(eventId)
                if (eventResponse.isSuccessful) {
                    val evento = eventResponse.body()
                    evento?.let {
                        title = it.nombre
                        location = it.ubicacion
                        capacity = it.capacidadMaxima.toString()
                        description = it.descripcion

                        // Parse ISO datetime strings
                        val startDateTime = LocalDateTime.parse(it.fechaInicio, DateTimeFormatter.ISO_DATE_TIME)
                        val endDateTime = LocalDateTime.parse(it.fechaFin, DateTimeFormatter.ISO_DATE_TIME)

                        startDate = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        endDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        endTime = endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                } else {
                    errorMessage = "Error al cargar evento"
                }
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            EditEventField(label = "TÍTULO DEL EVENTO", value = title, onValueChange = { title = it }, icon = "book-open-reader.png")

                            Text(text = "FECHA DE INICIO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = startDate,
                                    onValueChange = { startDate = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("YYYY-MM-DD") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF5F6FA),
                                        unfocusedContainerColor = Color(0xFFF5F6FA),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                                OutlinedTextField(
                                    value = startTime,
                                    onValueChange = { startTime = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("HH:mm") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF5F6FA),
                                        unfocusedContainerColor = Color(0xFFF5F6FA),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "FECHA DE FIN", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = endDate,
                                    onValueChange = { endDate = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("YYYY-MM-DD") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF5F6FA),
                                        unfocusedContainerColor = Color(0xFFF5F6FA),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                                OutlinedTextField(
                                    value = endTime,
                                    onValueChange = { endTime = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("HH:mm") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF5F6FA),
                                        unfocusedContainerColor = Color(0xFFF5F6FA),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            EditEventField(label = "UBICACIÓN", value = location, onValueChange = { location = it }, icon = "home.png")
                            EditEventField(label = "CAPACIDAD MÁXIMA", value = capacity, onValueChange = { capacity = it }, icon = "user.png")

                            Text(text = "DESCRIPCIÓN", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF5F6FA),
                                    unfocusedContainerColor = Color(0xFFF5F6FA),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    isSaving = true

                                    val sharedPref = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                                    val token = sharedPref.getString("token", "") ?: ""

                                    // Build ISO datetime strings
                                    val fechaInicio = "${startDate}T${startTime}:00"
                                    val fechaFin = "${endDate}T${endTime}:00"

                                    val updateBody = mapOf(
                                        "nombre" to title,
                                        "ubicacion" to location,
                                        "capacidadMaxima" to (capacity.toIntOrNull() ?: 0),
                                        "descripcion" to description,
                                        "fechaInicio" to fechaInicio,
                                        "fechaFin" to fechaFin
                                    )

                                    val response = ApiClient.apiService.actualizarEvento("Bearer $token", eventId, updateBody)
                                    isSaving = false

                                    if (response.isSuccessful) {
                                        onSave()
                                    } else {
                                        errorMessage = "Error al guardar cambios"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                    isSaving = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditEventField(label: String, value: String, onValueChange: (String) -> Unit, icon: String? = null) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F6FA),
                unfocusedContainerColor = Color(0xFFF5F6FA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = if (icon != null) {
                {
                    Image(
                        bitmap = assetImageBitmap(icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdminEditEventScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminEditEventScreen(eventId = 1)
    }
}