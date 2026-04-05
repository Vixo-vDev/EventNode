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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.util.Base64
import org.json.JSONObject
import androidx.compose.ui.window.Dialog

@Composable
fun StudentEventDetailScreen(
    eventId: Int = -1,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onCancelSuccess: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showInscribirseDialog by remember { mutableStateOf(false) }
    var showInscripcionSuccess by remember { mutableStateOf(false) }
    var evento by remember { mutableStateOf<EventoResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isCancelling by remember { mutableStateOf(false) }
    var isCheckingIn by remember { mutableStateOf(false) }
    var isEnrolled by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var inscritosCount by remember { mutableStateOf<Int?>(null) }
    var cancelErrorMsg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE)
    val usuarioId = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""
    val bearerToken = if (token.isNotEmpty()) "Bearer $token" else ""

    LaunchedEffect(eventId) {
        if (eventId == -1) {
            errorMessage = "ID de evento no válido"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            // Fetch Event Details
            val response = ApiClient.apiService.getEvento(eventId)
            if (response.isSuccessful) {
                evento = response.body()

                // Fetch enrolled count for capacity display
                if (bearerToken.isNotEmpty()) {
                    try {
                        val countResponse = ApiClient.apiService.contarInscritos(bearerToken, eventId)
                        if (countResponse.isSuccessful) {
                            val countData = countResponse.body()
                            inscritosCount = (countData?.get("totalInscritos") as? Number)?.toInt()
                                ?: (countData?.get("total") as? Number)?.toInt()
                                ?: (countData?.get("count") as? Number)?.toInt()
                        }
                    } catch (_: Exception) { /* Non-critical: capacity count failed */ }
                }

                // Fetch user's enrolled events to determine button state
                if (usuarioId != -1 && bearerToken.isNotEmpty()) {
                    val enrollResponse = ApiClient.apiService.listarMisEventos(bearerToken, usuarioId)
                    if (enrollResponse.isSuccessful) {
                        val misEventos = enrollResponse.body() ?: emptyList()
                        isEnrolled = misEventos.any {
                            (it["idEvento"] as? Double)?.toInt() == eventId &&
                            it["inscripcionEstado"] == "ACTIVO"
                        }
                    }
                }
            } else {
                errorMessage = "No se pudo cargar la información del evento"
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión"
        } finally {
            isLoading = false
        }
    }

    // Helper function to decode base64 images that might start with 'data:image/...;base64,'
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

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 90.dp)
            ) {
                // Image Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFF7FA7C6)) // Placeholder color
                ) {
                    val bitmap = decodeBase64Image(evento?.banner)
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Banner del evento",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                    
                    // Back button
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = assetImageBitmap("arrow-small-left.png"),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF333333))
                        )
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Cargando detalles...")
                    }
                } else if (evento == null && errorMessage != null) {
                   Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                } else if (evento != null) {
                    val ev = evento!!
                    // Event Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 20.dp),
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
                                text = ev.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            if (ev.categoriaNombre != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE6F0FF))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = ev.categoriaNombre.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF2F6FED),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        val fechaStr = if (ev.fechaInicio.length >= 16) {
                            ev.fechaInicio.substring(0, 10).replace("-", "/") + " - " + ev.fechaInicio.substring(11, 16)
                        } else {
                            ev.fechaInicio
                        }

                        DetailItem(
                            label = "FECHA Y HORA",
                            value = fechaStr,
                            icon = "chart-histogram.png"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        DetailItem(
                            label = "UBICACIÓN",
                            value = ev.ubicacion,
                            icon = "book-open-reader.png"
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Información del Evento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = ev.descripcion ?: "Sin descripción",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            lineHeight = 20.sp
                        )
                        
                        // Capacity display
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0F7FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("user.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "CAPACIDAD",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF999999),
                                    fontWeight = FontWeight.Bold
                                )
                                val capacityText = if (inscritosCount != null) {
                                    "${inscritosCount}/${ev.capacidadMaxima} lugares"
                                } else {
                                    "${ev.capacidadMaxima} lugares disponibles"
                                }
                                Text(
                                    text = capacityText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF333333)
                                )
                            }
                        }

                        // Error message display
                        if (errorMessage != null && !isLoading) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFFEF5350),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        if (isEnrolled) {
                            Button(
                                onClick = { cancelErrorMsg = null; showCancelDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                                enabled = !isCancelling
                            ) {
                                Text(
                                    if (isCancelling) "Cancelando..." else "Cancelar inscripción",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        } else if (ev.estado == "ACTIVO" || ev.estado == "PRÓXIMO") {
                            Button(
                                onClick = { errorMessage = null; showInscribirseDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                                enabled = !isCheckingIn
                            ) {
                                Text(
                                    if (isCheckingIn) "Inscribiendo..." else "Inscribirme",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        } else {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                enabled = false
                            ) {
                                Text(
                                    "Evento ${ev.estado}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
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
                StudentBottomNav(selected = "Agenda", onHome = onHome, onAgenda = onAgenda, onDiplomas = onDiplomas, onProfile = onProfile)
            }

            // Inscribirme Confirmation Dialog (DFR 3.1)
            if (showInscribirseDialog && evento != null) {
                val ev = evento!!
                Dialog(onDismissRequest = { if (!isCheckingIn) showInscribirseDialog = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .widthIn(max = 340.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF0F7FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("book-open-reader.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "¿Confirmar inscripción?",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "¿Estás seguro de que deseas inscribirte al evento \"${ev.nombre}\"?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        isCheckingIn = true
                                        errorMessage = null
                                        try {
                                            val body = mapOf("idUsuario" to usuarioId, "idEvento" to ev.idEvento)
                                            val res = ApiClient.apiService.inscribirse(bearerToken, body)
                                            if (res.isSuccessful) {
                                                isEnrolled = true
                                                showInscribirseDialog = false
                                                showInscripcionSuccess = true
                                                // Refresh inscribed count
                                                try {
                                                    val countRes = ApiClient.apiService.contarInscritos(bearerToken, eventId)
                                                    if (countRes.isSuccessful) {
                                                        val d = countRes.body()
                                                        inscritosCount = (d?.get("total") as? Number)?.toInt()
                                                            ?: (d?.get("count") as? Number)?.toInt()
                                                            ?: (d?.get("inscritos") as? Number)?.toInt()
                                                    }
                                                } catch (_: Exception) {}
                                            } else {
                                                val errBody = res.errorBody()?.string()
                                                val msg = try {
                                                    JSONObject(errBody ?: "").optString("mensaje", "")
                                                } catch (_: Exception) { "" }
                                                errorMessage = msg.ifBlank {
                                                    when (res.code()) {
                                                        409 -> "Ya cuenta con un lugar en este evento"
                                                        400 -> "El pre-check-in ya no está disponible"
                                                        else -> "No se pudo completar la inscripción"
                                                    }
                                                }
                                                showInscribirseDialog = false
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Error de conexión"
                                            showInscribirseDialog = false
                                        } finally {
                                            isCheckingIn = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                                enabled = !isCheckingIn
                            ) {
                                Text(if (isCheckingIn) "Procesando..." else "Confirmar inscripción", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { showInscribirseDialog = false },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
                            ) {
                                Text("Cancelar", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Inscripción Success Dialog
            if (showInscripcionSuccess) {
                DialogOverlay {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .widthIn(max = 340.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2F6FED)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "¡Inscripción exitosa!",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Te has inscrito correctamente al evento. Recuerda asistir y hacer check-in el día del evento.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = { showInscripcionSuccess = false },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                            ) {
                                Text("Aceptar", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Cancel Dialog with proper error handling (DFR 3.2)
            if (showCancelDialog && evento != null) {
                val ev = evento!!
                DialogOverlay {
                    CancelDialog(
                        isCancelling = isCancelling,
                        cancelError = cancelErrorMsg,
                        onConfirm = {
                            scope.launch {
                                isCancelling = true
                                cancelErrorMsg = null
                                try {
                                    val body = mapOf("idUsuario" to usuarioId, "idEvento" to ev.idEvento)
                                    val res = ApiClient.apiService.cancelarInscripcion(bearerToken, body)
                                    if (res.isSuccessful) {
                                        showCancelDialog = false
                                        showSuccessDialog = true
                                        isEnrolled = false
                                        // Refresh inscribed count
                                        try {
                                            val countRes = ApiClient.apiService.contarInscritos(bearerToken, eventId)
                                            if (countRes.isSuccessful) {
                                                val d = countRes.body()
                                                inscritosCount = (d?.get("total") as? Number)?.toInt()
                                                    ?: (d?.get("count") as? Number)?.toInt()
                                                    ?: (d?.get("inscritos") as? Number)?.toInt()
                                            }
                                        } catch (_: Exception) {}
                                    } else {
                                        val errBody = res.errorBody()?.string()
                                        val msg = try {
                                            JSONObject(errBody ?: "").optString("mensaje", "")
                                        } catch (_: Exception) { "" }
                                        cancelErrorMsg = msg.ifBlank { "Ya no es posible cancelar su asistencia" }
                                    }
                                } catch(e: Exception) {
                                    cancelErrorMsg = "Error de conexión"
                                } finally {
                                    isCancelling = false
                                }
                            }
                        },
                        onDismiss = { showCancelDialog = false }
                    )
                }
            }

            // Cancel Success Dialog
            if (showSuccessDialog) {
                DialogOverlay {
                    SuccessDialog(
                        onHome = onHome,
                        onOtherEvents = {
                            showSuccessDialog = false
                            onHome()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, icon: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F7FF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = assetImageBitmap(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
private fun DialogOverlay(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CancelDialog(isCancelling: Boolean, cancelError: String? = null, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .widthIn(max = 340.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F7FF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("chart-histogram.png"),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Estás seguro de que deseas cancelar tu registro?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Esta acción eliminará lógicamente tu registro. Ten en cuenta que puedes cancelar tu registro dentro del tiempo permitido.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            // Show cancel error message (DFR 3.2)
            if (cancelError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = cancelError,
                    color = Color(0xFFEF5350),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                enabled = !isCancelling
            ) {
                Text(if (isCancelling) "Procesando..." else "Confirmar cancelación", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
            ) {
                Text("Mantener registro", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SuccessDialog(onHome: () -> Unit, onOtherEvents: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .widthIn(max = 340.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2F6FED)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White))
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "¡Cancelación exitosa!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tu registro al evento ha sido eliminado correctamente de nuestra base de datos. Lamentamos que no puedas asistir.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
            ) {
                Text("Volver al Inicio", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onOtherEvents,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
            ) {
                Text("Ver otros eventos", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StudentBottomNav(selected: String, onHome: () -> Unit, onAgenda: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(
                label = "Inicio",
                icon = "home.png",
                selected = selected == "Inicio",
                onClick = onHome
            )
            BottomNavItem(
                label = "Agenda",
                icon = "book-open-reader.png",
                selected = selected == "Agenda",
                onClick = onAgenda
            )
            BottomNavItem(
                label = "Diplomas",
                icon = "diploma.png",
                selected = selected == "Diplomas",
                onClick = onDiplomas
            )
            BottomNavItem(
                label = "Perfil",
                icon = "user.png",
                selected = selected == "Perfil",
                onClick = onProfile
            )
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B)
    val tint = ColorFilter.tint(if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            bitmap = assetImageBitmap(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            colorFilter = tint
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun StudentEventDetailScreenPreview() {
    IntegradoraEventNodeTheme {
        StudentEventDetailScreen()
    }
}
