package mx.edu.utez.integradoraeventnode.ui.screens.admin.diplomas

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav
import android.content.Context

enum class DiplomaAdminState {
    MENU
}

data class DiplomaItem(
    val idDiploma: Int,
    val nombreEvento: String,
    val firma: String?,
    val diseno: String?,
    val tienePlantilla: Boolean,
    val tieneFirma: Boolean,
    val fechaCreacion: String?,
    val estado: String?,
    val totalEmitidos: Int,
    val totalPendientes: Int
)

@Composable
fun AdminDiplomasScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var diplomas by remember { mutableStateOf<List<DiplomaItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successCount by remember { mutableStateOf(0) }

    val sharedPrefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
    val token = sharedPrefs.getString("token", "") ?: ""

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                val response = ApiClient.apiService.listarDiplomas("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    diplomas = response.body()!!.map { map ->
                        DiplomaItem(
                            idDiploma = (map["idDiploma"] as? Number)?.toInt() ?: 0,
                            nombreEvento = map["nombreEvento"] as? String ?: "Sin nombre",
                            firma = map["firma"] as? String,
                            diseno = map["diseno"] as? String,
                            tienePlantilla = map["tienePlantilla"] as? Boolean ?: false,
                            tieneFirma = map["tieneFirma"] as? Boolean ?: false,
                            fechaCreacion = map["fechaCreacion"] as? String,
                            estado = map["estado"] as? String,
                            totalEmitidos = (map["totalEmitidos"] as? Number)?.toInt() ?: 0,
                            totalPendientes = (map["totalPendientes"] as? Number)?.toInt() ?: 0
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

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                        ErrorView(message = errorMessage ?: "Error desconocido")
                    }
                    diplomas.isEmpty() -> {
                        EmptyDiplomasAdminView()
                    }
                    else -> {
                        DiplomasAdminListView(
                            diplomas = diplomas,
                            token = token,
                            onEmitSuccess = { count ->
                                successCount = count
                                showSuccessDialog = true
                            }
                        )
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
                    selected = "Diplomas",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = {},
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }

    if (showSuccessDialog) {
        DiplomaSuccessDialog(
            count = successCount,
            onDismiss = { showSuccessDialog = false }
        )
    }
}

@Composable
private fun EmptyDiplomasAdminView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sin diplomas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "No hay diplomas configurados. Crea uno en la web para empezar.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorView(message: String) {
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
private fun DiplomasAdminListView(
    diplomas: List<DiplomaItem>,
    token: String,
    onEmitSuccess: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Gestión de Diplomas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        diplomas.forEach { diploma ->
            DiplomaAdminCard(
                diploma = diploma,
                token = token,
                onEmitSuccess = onEmitSuccess
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DiplomaAdminCard(
    diploma: DiplomaItem,
    token: String,
    onEmitSuccess: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isEmitting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = diploma.nombreEvento,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BadgeItem(
                            label = if (diploma.tienePlantilla) "✓ Plantilla" else "Sin plantilla",
                            color = if (diploma.tienePlantilla) Color(0xFF4CAF50) else Color(0xFFBDBDBD)
                        )
                        BadgeItem(
                            label = if (diploma.tieneFirma) "✓ Firma" else "Sin firma",
                            color = if (diploma.tieneFirma) Color(0xFF4CAF50) else Color(0xFFBDBDBD)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎓", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Emitidos", value = diploma.totalEmitidos.toString())
                StatItem(label = "Pendientes", value = diploma.totalPendientes.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            isEmitting = true
                            val response = ApiClient.apiService.emitirDiplomas(
                                "Bearer $token",
                                diploma.idDiploma
                            )
                            if (response.isSuccessful && response.body() != null) {
                                val count = (response.body()!!["enviados"] as? Number)?.toInt() ?: 0
                                onEmitSuccess(count)
                            } else {
                                Toast.makeText(context, "Error al emitir diplomas", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isEmitting = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                enabled = !isEmitting
            ) {
                if (isEmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Emitir Diplomas", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.2f),
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF2F6FED)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun DiplomaSuccessDialog(count: Int, onDismiss: () -> Unit) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color(0xFF4CAF50), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¡Diplomas Emitidos!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Se emitieron $count diploma(s) exitosamente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDiplomasScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminDiplomasScreen()
    }
}
