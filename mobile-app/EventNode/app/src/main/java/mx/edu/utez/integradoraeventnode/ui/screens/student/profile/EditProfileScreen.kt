package mx.edu.utez.integradoraeventnode.ui.screens.student.profile

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE)
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(prefs.getString("nombre", "") ?: "") }
    var apellidoPaterno by remember { mutableStateOf(prefs.getString("apellidoPaterno", "") ?: "") }
    var apellidoMaterno by remember { mutableStateOf(prefs.getString("apellidoMaterno", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("correo", "") ?: "") }
    var enrollment by remember { mutableStateOf(prefs.getString("matricula", "") ?: "") }
    var gender by remember { mutableStateOf(prefs.getString("sexo", "") ?: "") }
    var quarter by remember { mutableStateOf(prefs.getInt("cuatrimestre", 0).let { if (it > 0) "${it}°" else "" }) }
    var edad by remember { mutableStateOf(prefs.getInt("edad", 0).let { if (it > 0) it.toString() else "" }) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val idUsuario = prefs.getInt("id", -1)
    val token = prefs.getString("token", "") ?: ""
    val bearerToken = if (token.isNotEmpty()) "Bearer $token" else ""

    // Validation function
    fun validate(): String? {
        if (name.isBlank()) return "El nombre no puede estar vacío"
        if (apellidoPaterno.isBlank()) return "El apellido paterno no puede estar vacío"
        if (apellidoMaterno.isBlank()) return "El apellido materno no puede estar vacío"
        if (email.isBlank()) return "El correo no puede estar vacío"
        val emailPattern = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        if (!emailPattern.matches(email)) return "Ingrese un correo electrónico válido"
        if (enrollment.isBlank()) return "La matrícula no puede estar vacía"
        if (edad.isBlank()) return "La edad no puede estar vacía"
        val edadInt = edad.toIntOrNull()
        if (edadInt == null || edadInt < 17 || edadInt > 99) return "La edad debe estar entre 17 y 99 años"
        if (gender.isBlank()) return "Seleccione un sexo"
        val cuatrimestreNum = quarter.replace("°", "").toIntOrNull()
        if (cuatrimestreNum == null || cuatrimestreNum < 1 || cuatrimestreNum > 10) return "Seleccione un cuatrimestre válido (1-10)"
        return null
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 20.dp)
            ) {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Image(
                                bitmap = assetImageBitmap("arrow-small-left.png"),
                                contentDescription = "Regresar",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Editar Perfil",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Profile Image Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            bitmap = assetImageBitmap("user.png"),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2F6FED))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = assetImageBitmap("vista.png"),
                                contentDescription = "Editar foto",
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }

                // Error message
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF5350),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                // Main Info Form
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        EditField(label = "NOMBRE", value = name, onValueChange = { name = it }, icon = "user.png")
                        EditField(label = "APELLIDO PATERNO", value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, icon = "user.png")
                        EditField(label = "APELLIDO MATERNO", value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, icon = "user.png")
                        EditField(label = "CORREO", value = email, onValueChange = { email = it }, icon = "correo.png")
                        EditField(label = "MATRÍCULA", value = enrollment, onValueChange = { enrollment = it }, icon = "diploma.png")
                        EditField(label = "EDAD", value = edad, onValueChange = { edad = it }, icon = "user.png")

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            EditField(
                                label = "SEXO",
                                value = gender,
                                onValueChange = { gender = it },
                                modifier = Modifier.weight(1f),
                                isDropdown = true,
                                dropdownOptions = listOf("Masculino", "Femenino", "Otro")
                            )
                            EditField(
                                label = "CUATRIMESTRE",
                                value = quarter,
                                onValueChange = { quarter = it },
                                modifier = Modifier.weight(1f),
                                isDropdown = true,
                                dropdownOptions = listOf("1°", "2°", "3°", "4°", "5°", "6°", "7°", "8°", "9°", "10°")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bottom Save Button
                Button(
                    onClick = {
                        errorMessage = null
                        // Validate fields
                        val validationError = validate()
                        if (validationError != null) {
                            errorMessage = validationError
                            return@Button
                        }

                        isSaving = true
                        scope.launch {
                            try {
                                val cuatrimestreNum = quarter.replace("°", "").toIntOrNull() ?: 0
                                val edadInt = edad.toIntOrNull() ?: 0

                                val body = mutableMapOf<String, Any>(
                                    "nombre" to name,
                                    "apellidoPaterno" to apellidoPaterno,
                                    "apellidoMaterno" to apellidoMaterno,
                                    "correo" to email,
                                    "matricula" to enrollment,
                                    "sexo" to gender,
                                    "cuatrimestre" to cuatrimestreNum,
                                    "edad" to edadInt
                                )

                                val response = ApiClient.apiService.actualizarAlumno(
                                    bearerToken,
                                    idUsuario,
                                    body
                                )

                                if (response.isSuccessful) {
                                    // Update SharedPreferences with new data
                                    prefs.edit()
                                        .putString("nombre", name)
                                        .putString("apellidoPaterno", apellidoPaterno)
                                        .putString("apellidoMaterno", apellidoMaterno)
                                        .putString("correo", email)
                                        .putString("matricula", enrollment)
                                        .putString("sexo", gender)
                                        .putInt("cuatrimestre", cuatrimestreNum)
                                        .putInt("edad", edadInt)
                                        .apply()

                                    showSuccessDialog = true
                                } else {
                                    val errBody = response.errorBody()?.string()
                                    val msg = try {
                                        JSONObject(errBody ?: "").optString("mensaje", "")
                                    } catch (_: Exception) { "" }
                                    errorMessage = msg.ifBlank { "Error al actualizar el perfil" }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error de conexión"
                            } finally {
                                isSaving = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                    enabled = !isSaving
                ) {
                    Text(
                        if (isSaving) "Guardando..." else "Guardar datos",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        SuccessDialog(
            onBackToProfile = {
                showSuccessDialog = false
                onBack()
            },
            onGoHome = {
                showSuccessDialog = false
                onHome()
            }
        )
    }
}

@Composable
private fun SuccessDialog(onBackToProfile: () -> Unit, onGoHome: () -> Unit) {
    Dialog(onDismissRequest = onBackToProfile) {
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
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF2F6FED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Perfil actualizado correctamente", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu información ha sido guardada correctamente en nuestro sistema. Los cambios ya son visibles en tu perfil.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onBackToProfile,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Volver al Perfil", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onGoHome,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
                ) {
                    Text("Ir al Inicio", color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDropdown: Boolean = false,
    dropdownOptions: List<String> = emptyList(),
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    icon: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF7A7A7A), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (isDropdown) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8F9FB),
                        unfocusedContainerColor = Color(0xFFF8F9FB),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    dropdownOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8F9FB),
                    unfocusedContainerColor = Color(0xFFF8F9FB),
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
                    } else null,
                    trailingIcon = if (isPassword) {
                        {
                            IconButton(onClick = onPasswordToggle) {
                                val iconName = if (passwordVisible) "vista.png" else "eye.png"
                                Image(
                                    bitmap = assetImageBitmap(iconName),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    IntegradoraEventNodeTheme {
        EditProfileScreen()
    }
}
