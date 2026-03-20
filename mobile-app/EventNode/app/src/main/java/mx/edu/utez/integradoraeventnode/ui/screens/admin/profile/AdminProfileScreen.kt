package mx.edu.utez.integradoraeventnode.ui.screens.admin.profile

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun AdminProfileScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // State for user data from SharedPreferences
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var cuatrimestre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }

    // State for diplomas from API
    var diplomas by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoadingDiplomas by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Load user data from SharedPreferences
    LaunchedEffect(Unit) {
        val sharedPrefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
        nombre = sharedPrefs.getString("nombre", "") ?: ""
        apellidoPaterno = sharedPrefs.getString("apellidoPaterno", "") ?: ""
        apellidoMaterno = sharedPrefs.getString("apellidoMaterno", "") ?: ""
        correo = sharedPrefs.getString("correo", "") ?: ""
        matricula = sharedPrefs.getString("matricula", "") ?: ""
        sexo = sharedPrefs.getString("sexo", "") ?: ""
        cuatrimestre = sharedPrefs.getString("cuatrimestre", "") ?: ""
        rol = sharedPrefs.getString("rol", "") ?: ""
        token = sharedPrefs.getString("token", "") ?: ""
        userId = sharedPrefs.getInt("id", 0)

        // Load diplomas managed by admin
        if (token.isNotEmpty()) {
            isLoadingDiplomas = true
            coroutineScope.launch {
                try {
                    val response = ApiClient.apiService.listarDiplomas("Bearer $token")
                    if (response.isSuccessful) {
                        diplomas = response.body() ?: emptyList()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoadingDiplomas = false
                }
            }
        }
    }

    // Map gender code to Spanish text
    val generoTexto = when (sexo) {
        "M" -> "Masculino"
        "F" -> "Femenino"
        else -> sexo
    }

    // Get initials for avatar
    val initials = (nombre.firstOrNull()?.uppercase() ?: "") + (apellidoPaterno.firstOrNull()?.uppercase() ?: "")

    // Build full name
    val nombreCompleto = "$nombre $apellidoPaterno $apellidoMaterno".trim()

    // Map role to display text
    val rolTexto = when (rol.uppercase()) {
        "ADMIN", "ADMINISTRADOR" -> "ADMINISTRADOR"
        "SUPERADMIN" -> "SUPERADMIN"
        else -> rol.uppercase()
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Profile Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar with initials
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2F6FED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onEditProfile,
                        modifier = Modifier
                            .height(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F7FF)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Image(
                            bitmap = assetImageBitmap("vista.png"),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Editar Perfil", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = nombreCompleto,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2F6FED)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Role badge
                    Surface(
                        modifier = Modifier
                            .background(Color(0xFFF0F7FF), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFFF0F7FF),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = rolTexto,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F6FED),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Datos del Administrador Section
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(text = "Datos del Administrador", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileInfoItem(label = "Nombre completo:", value = nombreCompleto, icon = "user.png")
                    ProfileInfoItem(label = "Correo:", value = correo, icon = "correo.png")
                    ProfileInfoItem(label = "Matrícula:", value = matricula, icon = "user.png")
                    ProfileInfoItem(label = "Género:", value = generoTexto, icon = "user.png")
                    if (cuatrimestre.isNotEmpty()) {
                        ProfileInfoItem(label = "Cuatrimestre:", value = cuatrimestre, icon = "user.png")
                    }
                    ProfileInfoItem(label = "Rol:", value = rolTexto, icon = "user.png")

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "Certificados emitidos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoadingDiplomas) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = Color(0xFF2F6FED)
                            )
                        }
                    } else if (diplomas.isEmpty()) {
                        Text(
                            text = "No hay certificados emitidos",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        diplomas.forEachIndexed { index, diploma ->
                            val fechaRaw = (diploma["fechaCreacion"] ?: diploma["fechaEnvio"])?.toString() ?: ""
                            val fecha = if (fechaRaw.length >= 10) fechaRaw.substring(0, 10) else fechaRaw
                            val titulo = (diploma["nombreEvento"] ?: "Certificado").toString()

                            CertificadoItem(date = fecha, title = titulo)
                            if (index < diplomas.size - 1) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cerrar Sesión", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
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
                    selected = "Perfil",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = {}
                )
            }
        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }
}

@Composable
private fun ProfileInfoItem(label: String, value: String, icon: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F7FF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = assetImageBitmap(icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = Color(0xFF2F6FED), fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CertificadoItem(date: String, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F6FA)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("diploma.png"),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = title, fontSize = 12.sp, color = Color(0xFF2F6FED))
            }
        }
    }
}

@Composable
private fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
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
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = assetImageBitmap("home.png"), // Placeholder para icono de salida
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¿Cerrar sesión?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¿Estás seguro de que deseas salir de tu cuenta en EventNode? Tendrás que volver a ingresar tus credenciales para acceder.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Sí, cerrar sesión", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Mantener sesión", color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProfileScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminProfileScreen()
    }
}