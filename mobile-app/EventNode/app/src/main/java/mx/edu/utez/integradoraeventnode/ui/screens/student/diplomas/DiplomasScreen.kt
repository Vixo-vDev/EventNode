package mx.edu.utez.integradoraeventnode.ui.screens.student.diplomas

import android.graphics.BitmapFactory
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
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
import mx.edu.utez.integradoraeventnode.ui.screens.student.profile.ProfileBottomNav
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale

data class StudentDiplomaData(
    val idEmitido: Int,
    val nombreEvento: String,
    val firma: String?,
    val fechaEnvio: String?,
    val estadoEnvio: String?
)

@Composable
fun DiplomasScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var diplomas by remember { mutableStateOf<List<StudentDiplomaData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val sharedPrefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
    val token = sharedPrefs.getString("token", "") ?: ""
    val userId = sharedPrefs.getInt("id", 0)

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                val response = ApiClient.apiService.listarDiplomasEstudiante("Bearer $token", userId)
                if (response.isSuccessful && response.body() != null) {
                    diplomas = response.body()!!.map { map ->
                        StudentDiplomaData(
                            idEmitido = (map["idEmitido"] as? Number)?.toInt() ?: 0,
                            nombreEvento = map["nombreEvento"] as? String ?: "Sin nombre",
                            firma = map["firma"] as? String,
                            fechaEnvio = map["fechaEnvio"] as? String,
                            estadoEnvio = map["estadoEnvio"] as? String
                        )
                    }
                } else {
                    errorMessage = "Error al cargar diplomas"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = Color(0xFF2F6FED)
                            )
                        }
                    }
                    errorMessage != null -> {
                        ErrorStudentView(message = errorMessage ?: "Error desconocido")
                    }
                    diplomas.isEmpty() -> {
                        EmptyDiplomasState(onExplore = onHome)
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Tus diplomas ganados",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Has completado ${diplomas.size} curso(s)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF7A7A7A)
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            diplomas.forEachIndexed { index, diploma ->
                                StudentDiplomaCard(
                                    diploma = diploma,
                                    colorIndex = index
                                )
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
                ProfileBottomNav(
                    currentScreen = "Diplomas",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onDiplomas = {},
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun ErrorStudentView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyDiplomasState(onExplore: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 340.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = assetImageBitmap("Gemini_Generated_Image_lf9n0ilf9n0ilf9n.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Aún no hay\ndiplomas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No has completado ningún evento todavía. Explora eventos para comenzar a obtener certificados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7A7A7A),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onExplore,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Explorar eventos", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StudentDiplomaCard(diploma: StudentDiplomaData, colorIndex: Int) {
    val context = LocalContext.current

    val cardColors = listOf(
        Color(0xFF6F9EA6),
        Color(0xFFD1B0A0),
        Color(0xFFE5D1B8),
        Color(0xFFA5B8D1),
        Color(0xFFD1A5B8),
        Color(0xFFB8D1A5)
    )
    val cardColor = cardColors[colorIndex % cardColors.size]

    val formattedDate = diploma.fechaEnvio?.let { dateStr ->
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
            val date = inputFormat.parse(dateStr)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    } ?: "Fecha no disponible"

    val statusColor = when (diploma.estadoEnvio?.uppercase()) {
        "ENVIADO" -> Color(0xFFE8F5E9)
        "ERROR" -> Color(0xFFFFEBEE)
        else -> Color(0xFFFFF3E0)
    }

    val statusTextColor = when (diploma.estadoEnvio?.uppercase()) {
        "ENVIADO" -> Color(0xFF4CAF50)
        "ERROR" -> Color(0xFFC62828)
        else -> Color(0xFFFF9800)
    }

    val statusLabel = when (diploma.estadoEnvio?.uppercase()) {
        "ENVIADO" -> "Entregado"
        "ERROR" -> "Error"
        else -> "Pendiente"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(cardColor.copy(alpha = 0.8f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(120.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("CERTIFICATE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("FOR COMPLETION", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(40.dp).height(1.dp).background(Color.LightGray))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = diploma.nombreEvento,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusColor)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(statusTextColor))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusTextColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• Emisión: $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A7A7A)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Descarga disponible en la web",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Image(
                        bitmap = assetImageBitmap("qr-scan.png"),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Descargar PDF", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiplomasScreenPreview() {
    IntegradoraEventNodeTheme {
        DiplomasScreen()
    }
}
