package mx.edu.utez.integradoraeventnode.ui.screens.student.profile

import android.app.Activity
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import mx.edu.utez.integradoraeventnode.R
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.utils.LocaleHelper
import org.json.JSONObject

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE) }

    var nombre by remember { mutableStateOf(prefs.getString("nombre", "") ?: "") }
    var apellidoPaterno by remember { mutableStateOf(prefs.getString("apellidoPaterno", "") ?: "") }
    var apellidoMaterno by remember { mutableStateOf(prefs.getString("apellidoMaterno", "") ?: "") }
    var correo by remember { mutableStateOf(prefs.getString("correo", "") ?: "") }
    var matricula by remember { mutableStateOf(prefs.getString("matricula", "") ?: "") }
    var sexo by remember { mutableStateOf(prefs.getString("sexo", "") ?: "") }
    var cuatrimestre by remember { mutableStateOf(prefs.getInt("cuatrimestre", 0)) }
    var rol by remember { mutableStateOf(prefs.getString("rol", "ALUMNO") ?: "ALUMNO") }
    var edad by remember { mutableStateOf(prefs.getInt("edad", 0)) }

    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val idUsuario = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""

    // Load profile from API on screen launch
    LaunchedEffect(Unit) {
        if (idUsuario == -1 || token.isBlank()) {
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = ApiClient.apiService.obtenerPerfil("Bearer $token", idUsuario)
            if (response.isSuccessful) {
                val data = response.body() ?: emptyMap()
                val nuevoNombre = (data["nombre"] as? String) ?: nombre
                val nuevoApellidoPaterno = (data["apellidoPaterno"] as? String) ?: apellidoPaterno
                val nuevoApellidoMaterno = (data["apellidoMaterno"] as? String) ?: apellidoMaterno
                val nuevoCorreo = (data["correo"] as? String) ?: correo
                val nuevaMatricula = (data["matricula"] as? String) ?: matricula
                val nuevoSexo = (data["sexo"] as? String) ?: sexo
                val nuevoCuatrimestre = (data["cuatrimestre"] as? Number)?.toInt() ?: cuatrimestre
                val nuevoRol = (data["rol"] as? String) ?: rol
                val nuevaEdad = (data["edad"] as? Number)?.toInt() ?: edad

                // Update state
                nombre = nuevoNombre
                apellidoPaterno = nuevoApellidoPaterno
                apellidoMaterno = nuevoApellidoMaterno
                correo = nuevoCorreo
                matricula = nuevaMatricula
                sexo = nuevoSexo
                cuatrimestre = nuevoCuatrimestre
                rol = nuevoRol
                edad = nuevaEdad

                // Update SharedPreferences with fresh data
                prefs.edit()
                    .putString("nombre", nuevoNombre)
                    .putString("apellidoPaterno", nuevoApellidoPaterno)
                    .putString("apellidoMaterno", nuevoApellidoMaterno)
                    .putString("correo", nuevoCorreo)
                    .putString("matricula", nuevaMatricula)
                    .putString("sexo", nuevoSexo)
                    .putInt("cuatrimestre", nuevoCuatrimestre)
                    .putString("rol", nuevoRol)
                    .putInt("edad", nuevaEdad)
                    .apply()
            } else {
                errorMsg = "Error al cargar el perfil"
            }
        } catch (_: Exception) {
            errorMsg = "Error de conexión"
        } finally {
            isLoading = false
        }
    }

    val fullName = listOf(nombre, apellidoPaterno, apellidoMaterno)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Usuario" }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF2F6FED)
                )
            } else {
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
                    Box(modifier = Modifier.size(100.dp)) {
                        Image(
                            bitmap = assetImageBitmap("user.png"),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFD9D9D9))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onEditProfile,
                        modifier = Modifier
                            .height(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F7FF)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Image(
                            bitmap = assetImageBitmap("vista.png"),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar Perfil", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rol.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF2F6FED),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = correo.ifBlank { "Sin correo" },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A7A7A)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (matricula.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF0F2F5))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Matrícula: $matricula",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF555555),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Error message
                if (errorMsg != null) {
                    Text(
                        text = errorMsg!!,
                        color = Color(0xFFEF5350),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                // Personal Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Datos del Usuario",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(label = "Nombre", value = nombre.ifBlank { "N/A" }, icon = "user.png")
                    InfoRow(label = "Apellidos", value = "$apellidoPaterno $apellidoMaterno".trim().ifBlank { "N/A" }, icon = "user.png")
                    InfoRow(label = "Correo", value = correo.ifBlank { "N/A" }, icon = "correo.png")
                    if (matricula.isNotBlank()) InfoRow(label = "Matrícula", value = matricula, icon = "diploma.png")
                    if (edad > 0) InfoRow(label = "Edad", value = "$edad años", icon = "user.png")
                    if (sexo.isNotBlank()) InfoRow(label = "Sexo", value = sexo, icon = "user.png")
                    if (cuatrimestre > 0) InfoRow(label = "Cuatrimestre", value = "$cuatrimestre°", icon = "book-open-reader.png")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Change Password Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { showChangePasswordDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
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
                                bitmap = assetImageBitmap("lock.png"),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cambiar contraseña",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Actualiza tu contraseña de acceso",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF7A7A7A)
                            )
                        }
                        Image(
                            bitmap = assetImageBitmap("vista.png"),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFAAAAAA))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Language Selector
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(text = stringResource(R.string.language_title), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val currentLang = LocaleHelper.getCurrentLanguage(context)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F6FA), RoundedCornerShape(12.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("es" to stringResource(R.string.language_spanish), "en" to stringResource(R.string.language_english)).forEach { (code, label) ->
                            val isSelected = currentLang == code
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (!isSelected) {
                                            LocaleHelper.setLocale(context, code)
                                            (context as? Activity)?.recreate()
                                        }
                                    },
                                color = if (isSelected) Color(0xFF2F6FED) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = label,
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .fillMaxWidth(),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) {
                    Text(stringResource(R.string.profile_logout), style = MaterialTheme.typography.titleMedium, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom Nav
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ProfileBottomNav(onHome = onHome, onAgenda = onAgenda, onDiplomas = onDiplomas, onProfile = {})
            }
            } // close else block
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            correo = correo,
            onDismiss = { showChangePasswordDialog = false }
        )
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
                        bitmap = assetImageBitmap("home.png"), // Usando home.png como placeholder para el icono de salida
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = stringResource(R.string.profile_logout_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.profile_logout_msg),
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
                    Text(text = stringResource(R.string.profile_logout_confirm), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = stringResource(R.string.profile_keep_session), color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}


@Composable
private fun InfoRow(label: String, value: String, icon: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
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
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF555555),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    }
}

@Composable
fun ProfileBottomNav(currentScreen: String = "Perfil", onHome: () -> Unit, onAgenda: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(label = "Inicio", icon = "home.png", selected = currentScreen == "Inicio", onClick = onHome)
            BottomNavItem(label = "Agenda", icon = "book-open-reader.png", selected = currentScreen == "Agenda", onClick = onAgenda)
            BottomNavItem(label = "Diplomas", icon = "diploma.png", selected = currentScreen == "Diplomas", onClick = onDiplomas)
            BottomNavItem(label = "Perfil", icon = "user.png", selected = currentScreen == "Perfil", onClick = onProfile)
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

@Composable
private fun ChangePasswordDialog(correo: String, onDismiss: () -> Unit) {
    // Steps: "sendCode" -> "code" -> "newPassword" -> "success"
    var step by remember { mutableStateOf("sendCode") }
    var otpValue by remember { mutableStateOf(TextFieldValue("", TextRange(0))) }
    val otpFocus = remember { FocusRequester() }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showNewPwd by remember { mutableStateOf(false) }
    var showConfirmPwd by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(step) {
        if (step == "code") {
            otpValue = TextFieldValue("", TextRange(0))
            delay(120)
            otpFocus.requestFocus()
        }
    }

    Dialog(onDismissRequest = { if (!loading) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Step 1: Send Code ---
                if (step == "sendCode") {
                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF0F7FF)), contentAlignment = Alignment.Center) {
                        Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Cambiar contraseña", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Se enviará un código de verificación a tu correo:", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(correo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2F6FED), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))

                    if (error != null) {
                        Text(error!!, color = Color(0xFFEF5350), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 12.dp))
                    }

                    Button(
                        onClick = {
                            if (correo.isBlank()) { error = "No se encontró tu correo"; return@Button }
                            loading = true; error = null
                            scope.launch {
                                try {
                                    val resp = ApiClient.apiService.enviarCodigoRecuperacion(mapOf("correo" to correo))
                                    if (resp.isSuccessful) {
                                        step = "code"
                                    } else {
                                        val err = resp.errorBody()?.string()
                                        error = try { JSONObject(err ?: "").optString("mensaje", "Error al enviar código") } catch (_: Exception) { "Error al enviar código" }
                                    }
                                } catch (_: Exception) { error = "Error de conexión" }
                                finally { loading = false }
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text(if (loading) "Enviando..." else "Enviar código", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Cancelar", style = MaterialTheme.typography.labelLarge, color = Color(0xFF666666), modifier = Modifier.clickable { onDismiss() })
                }

                // --- Step 2: Verify Code ---
                if (step == "code") {
                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF0F7FF)), contentAlignment = Alignment.Center) {
                        Image(bitmap = assetImageBitmap("correo.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Verificar Código", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ingresa el código de 6 dígitos que enviamos a $correo", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))

                    if (error != null) {
                        Text(error!!, color = Color(0xFFEF5350), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 12.dp))
                    }

                    BasicTextField(
                        value = otpValue,
                        onValueChange = { tfv ->
                            if (loading) return@BasicTextField
                            val digits = tfv.text.filter { it.isDigit() }.take(6)
                            val sel = tfv.selection
                            val newCursor = if (!sel.collapsed) {
                                digits.length
                            } else {
                                minOf(sel.start, digits.length).coerceIn(0, digits.length)
                            }
                            otpValue = TextFieldValue(digits, TextRange(newCursor))
                            error = null
                            if (digits.length == 6) {
                                scope.launch {
                                    loading = true
                                    error = null
                                    try {
                                        val resp = ApiClient.apiService.verificarCodigo(
                                            mapOf("correo" to correo, "codigo" to digits)
                                        )
                                        if (resp.isSuccessful) {
                                            step = "newPassword"
                                        } else {
                                            val err = resp.errorBody()?.string()
                                            error = try {
                                                JSONObject(err ?: "").optString("mensaje", "Código incorrecto")
                                            } catch (_: Exception) {
                                                "Código incorrecto"
                                            }
                                            otpValue = TextFieldValue("", TextRange(0))
                                        }
                                    } catch (_: Exception) {
                                        error = "Error de conexión"
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(otpFocus),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        ),
                        decorationBox = { inner ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    for (i in 0..5) {
                                        val ch = otpValue.text.getOrNull(i)
                                        val filled = ch != null
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(52.dp)
                                                .clickable {
                                                    otpFocus.requestFocus()
                                                    val t = otpValue.text.take(i)
                                                    otpValue = TextFieldValue(t, TextRange(t.length))
                                                }
                                                .border(
                                                    1.dp,
                                                    if (filled) Color(0xFF2F6FED) else Color(0xFFE1E2EC),
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .background(
                                                    if (filled) Color(0xFFF0F7FF) else Color(0xFFF8F9FB),
                                                    RoundedCornerShape(10.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = ch?.toString() ?: "",
                                                style = TextStyle(
                                                    textAlign = TextAlign.Center,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 22.sp,
                                                    color = Color.Black
                                                )
                                            )
                                        }
                                    }
                                }
                                Box(Modifier.size(0.dp)) { inner() }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val codeStr = otpValue.text
                            if (codeStr.length != 6) {
                                error = "Ingresa el código completo"
                                return@Button
                            }
                            loading = true
                            error = null
                            scope.launch {
                                try {
                                    val resp = ApiClient.apiService.verificarCodigo(mapOf("correo" to correo, "codigo" to codeStr))
                                    if (resp.isSuccessful) {
                                        step = "newPassword"
                                    } else {
                                        val err = resp.errorBody()?.string()
                                        error = try {
                                            JSONObject(err ?: "").optString("mensaje", "Código incorrecto")
                                        } catch (_: Exception) {
                                            "Código incorrecto"
                                        }
                                        otpValue = TextFieldValue("", TextRange(0))
                                    }
                                } catch (_: Exception) {
                                    error = "Error de conexión"
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text(if (loading) "Verificando..." else "Verificar", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text("¿No recibiste el código? ", style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666))
                        Text("Reenviar", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF2F6FED),
                            modifier = Modifier.clickable {
                                loading = true; error = null
                                scope.launch {
                                    try {
                                        ApiClient.apiService.enviarCodigoRecuperacion(mapOf("correo" to correo))
                                        otpValue = TextFieldValue("", TextRange(0))
                                    } catch (_: Exception) { error = "Error al reenviar" }
                                    finally { loading = false }
                                }
                            })
                    }
                }

                // --- Step 3: New Password ---
                if (step == "newPassword") {
                    val hasMin = newPassword.length >= 8
                    val hasUpper = newPassword.any { it.isUpperCase() }
                    val hasSpecial = newPassword.any { !it.isLetterOrDigit() }
                    val match = newPassword == confirmPassword && newPassword.isNotEmpty()

                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF0F7FF)), contentAlignment = Alignment.Center) {
                        Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Nueva contraseña", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Crea una contraseña segura que no hayas utilizado antes.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))

                    if (error != null) {
                        Text(error!!, color = Color(0xFFEF5350), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 12.dp))
                    }

                    Text("Nueva contraseña", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it; error = null },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (showNewPwd) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.Gray)) },
                        trailingIcon = {
                            IconButton(onClick = { showNewPwd = !showNewPwd }) {
                                Image(bitmap = assetImageBitmap(if (showNewPwd) "vista.png" else "eye.png"), contentDescription = null, modifier = Modifier.size(20.dp))
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("REQUISITOS DE SEGURIDAD", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2F6FED), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileValidationRow("Mínimo 8 caracteres", hasMin)
                        ProfileValidationRow("Al menos una mayúscula", hasUpper)
                        ProfileValidationRow("Un carácter especial (#, \$, etc.)", hasSpecial)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Confirmar contraseña", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (showConfirmPwd) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.Gray)) },
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPwd = !showConfirmPwd }) {
                                Image(bitmap = assetImageBitmap(if (showConfirmPwd) "vista.png" else "eye.png"), contentDescription = null, modifier = Modifier.size(20.dp))
                            }
                        }
                    )
                    if (confirmPassword.isNotEmpty() && !match) {
                        Text("Las contraseñas no coinciden", color = Color(0xFFEF5350), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!hasMin || !hasUpper || !hasSpecial) { error = "La contraseña no cumple los requisitos"; return@Button }
                            if (!match) { error = "Las contraseñas no coinciden"; return@Button }
                            loading = true; error = null
                            scope.launch {
                                try {
                                    val resp = ApiClient.apiService.restablecerPassword(mapOf(
                                        "correo" to correo,
                                        "codigo" to otpValue.text,
                                        "nuevaPassword" to newPassword
                                    ))
                                    if (resp.isSuccessful) {
                                        step = "success"
                                    } else {
                                        val err = resp.errorBody()?.string()
                                        error = try { JSONObject(err ?: "").optString("mensaje", "Error") } catch (_: Exception) { "Error al restablecer" }
                                    }
                                } catch (_: Exception) { error = "Error de conexión" }
                                finally { loading = false }
                            }
                        },
                        enabled = !loading && hasMin && hasUpper && hasSpecial && match,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text(if (loading) "Restableciendo..." else "Cambiar contraseña", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cancelar", style = MaterialTheme.typography.labelLarge, color = Color(0xFF666666), modifier = Modifier.clickable { onDismiss() })
                }

                // --- Step 4: Success ---
                if (step == "success") {
                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                        Image(bitmap = assetImageBitmap("check-circle.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF4CAF50)))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("¡Contraseña Actualizada!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tu contraseña ha sido cambiada con éxito.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text("Listo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileValidationRow(text: String, isValid: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isValid) Color(0xFF4CAF50) else Color(0xFFCCCCCC))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isValid) Color(0xFF4CAF50) else Color(0xFF666666)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    IntegradoraEventNodeTheme {
        ProfileScreen()
    }
}
