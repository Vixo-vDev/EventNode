package mx.edu.utez.integradoraeventnode.ui.screens.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.AlumnoRegistroRequest
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import org.json.JSONObject
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var enrollment by remember { mutableStateOf("") }
    var institutionalEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf<LocalDate?>(null) }
    var sex by remember { mutableStateOf("Hombre") }
    var semester by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var expandedCuatrimestre by remember { mutableStateOf(false) }
    val cuatrimestres = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val age = remember(fechaNacimiento) {
        if (fechaNacimiento != null) {
            Period.between(fechaNacimiento, LocalDate.now()).years
        } else {
            0
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = assetImageBitmap("logo.png"),
                            contentDescription = "EventNode",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(vertical = 12.dp),
                            contentScale = ContentScale.Fit
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Regístrate para comenzar a usar EventNode.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF74777F),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Nombre(s)
                        Text(text = "Nombre(s)*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = name, onValueChange = { name = it }, placeholder = { Text("Ingresa tu nombre", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Apellido Paterno
                        Text(text = "Apellido paterno*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, placeholder = { Text("Ingresa primer apellido", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Apellido Materno
                        Text(text = "Apellido materno*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, placeholder = { Text("Ingresa segundo apellido", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Matrícula
                        Text(text = "Matrícula*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = enrollment, onValueChange = { enrollment = it }, placeholder = { Text("Ej: 20243ds01", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Correo institucional
                        Text(text = "Correo institucional*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = institutionalEmail, onValueChange = { institutionalEmail = it }, placeholder = { Text("matricula@utez.edu.mx", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Image(bitmap = assetImageBitmap("correo.png"), contentDescription = null, modifier = Modifier.size(20.dp)) },
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Contraseña
                        Text(text = "Contraseña*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = password, onValueChange = { password = it }, placeholder = { Text("********", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(20.dp)) },
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    val iconName = if (showPassword) "vista.png" else "eye.png"
                                    Image(bitmap = assetImageBitmap(iconName), contentDescription = null, modifier = Modifier.size(20.dp))
                                }
                            },
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mínimo 8 caracteres, mayúsculas, minúsculas, números y símbolos.",
                            style = MaterialTheme.typography.bodySmall, color = Color(0xFF999999), textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Confirmar Contraseña
                        Text(text = "Confirmar contraseña*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = { Text("********", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Image(bitmap = assetImageBitmap("lock.png"), contentDescription = null, modifier = Modifier.size(20.dp)) },
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    val iconName = if (showConfirmPassword) "vista.png" else "eye.png"
                                    Image(bitmap = assetImageBitmap(iconName), contentDescription = null, modifier = Modifier.size(20.dp))
                                }
                            },
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8F9FB), unfocusedContainerColor = Color(0xFFF8F9FB))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Fecha de Nacimiento
                        Text(text = "Fecha de nacimiento*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = fechaNacimiento?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
                            onValueChange = { },
                            readOnly = true,
                            placeholder = { Text("Seleccionar fecha", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth().clickable {
                                val now = LocalDate.now()
                                val dialog = DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        fechaNacimiento = LocalDate.of(year, month + 1, dayOfMonth)
                                    },
                                    fechaNacimiento?.year ?: now.year,
                                    (fechaNacimiento?.monthValue ?: now.monthValue) - 1,
                                    fechaNacimiento?.dayOfMonth ?: now.dayOfMonth
                                )
                                dialog.show()
                            },
                            enabled = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                disabledContainerColor = Color(0xFFF8F9FB), disabledTextColor = Color.Black,
                                disabledIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        if (age > 0) {
                            Text("Edad calculada: $age años", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2F6FED), modifier = Modifier.padding(top = 4.dp).fillMaxWidth())
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Sexo
                        Text(text = "Sexo*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = sex == "Hombre", onClick = { sex = "Hombre" }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2F6FED)))
                                Text("Hombre", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = sex == "Mujer", onClick = { sex = "Mujer" }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2F6FED)))
                                Text("Mujer", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        @OptIn(ExperimentalMaterial3Api::class)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Cuatrimestre
                            Text(text = "Cuatrimestre*", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF44474E), modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedCuatrimestre,
                                onExpandedChange = { expandedCuatrimestre = !expandedCuatrimestre },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = semester,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Seleccionar", color = Color(0xFF999999)) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCuatrimestre) },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                        focusedContainerColor = Color(0xFFF8F9FB),
                                        unfocusedContainerColor = Color(0xFFF8F9FB)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCuatrimestre,
                                    onDismissRequest = { expandedCuatrimestre = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    cuatrimestres.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                semester = selectionOption
                                                expandedCuatrimestre = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                // Validations
                                errorMessage = null
                                val emailPattern = "^[a-zA-Z0-9._-]+@utez\\.edu\\.mx$".toRegex()
                                val pwdPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_\\-]).{8,}$".toRegex()
                                
                                if (name.isBlank() || apellidoPaterno.isBlank() || apellidoMaterno.isBlank() || enrollment.isBlank() || institutionalEmail.isBlank() || password.isBlank() || confirmPassword.isBlank() || fechaNacimiento == null || semester.isBlank()) {
                                    errorMessage = "Todos los campos son obligatorios"
                                    return@Button
                                }
                                if (password != confirmPassword) {
                                    errorMessage = "Las contraseñas no coinciden"
                                    return@Button
                                }
                                if (!institutionalEmail.matches(emailPattern)) {
                                    errorMessage = "El correo debe tener terminación @utez.edu.mx"
                                    return@Button
                                }
                                if (!password.matches(pwdPattern)) {
                                    errorMessage = "La contraseña no es válida.\nDebe tener 8+ caracteres, mayúsculas, minúsculas, números y un símbolo."
                                    return@Button
                                }
                                if (age < 17 || age > 99) {
                                    errorMessage = "La edad ingresada no es válida para el registro académico"
                                    return@Button
                                }
                                val cuatriInt = semester.toIntOrNull()
                                if (cuatriInt == null || cuatrimestres.indexOf(semester) == -1) {
                                    errorMessage = "Cuatrimestre fuera de rango"
                                    return@Button
                                }

                                isLoading = true
                                scope.launch {
                                    try {
                                        val request = AlumnoRegistroRequest(
                                            nombre = name,
                                            apellidoPaterno = apellidoPaterno,
                                            apellidoMaterno = apellidoMaterno,
                                            matricula = enrollment,
                                            correo = institutionalEmail,
                                            password = password,
                                            fechaNacimiento = fechaNacimiento!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                            sexo = sex,
                                            cuatrimestre = cuatriInt
                                        )
                                        val response = ApiClient.apiService.registrarAlumno(request)
                                        if (response.isSuccessful) {
                                            showSuccess = true
                                        } else {
                                            val errorStr = response.errorBody()?.string()
                                            var msg = "Error al registrar"
                                            if (errorStr != null) {
                                                try {
                                                    val json = JSONObject(errorStr)
                                                    msg = json.optString("mensaje", "Error al registrar")
                                                    if (msg.contains("ya registrad")) {
                                                        msg = "Matrícula o correo ya registrados"
                                                    }
                                                } catch(e: Exception) {}
                                            }
                                            errorMessage = msg
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error de conexión: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                        ) {
                            Text(if (isLoading) "Enviando..." else "Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Inicia sesión", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2F6FED), modifier = Modifier.clickable { onBackToLogin() })
                        }
                    }
                }
            }

            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { errorMessage = null },
                    confirmButton = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("Aceptar", color = Color(0xFF2F6FED))
                        }
                    },
                    title = { Text("Aviso", fontWeight = FontWeight.Bold) },
                    text = { Text(errorMessage!!) },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            if (showSuccess) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0x80000000)).clickable(enabled = false) {}, contentAlignment = Alignment.Center) {
                    Card(modifier = Modifier.fillMaxWidth().padding(24.dp).widthIn(max = 360.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                                Image(bitmap = assetImageBitmap("check-circle.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF4CAF50)))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("¡Cuenta Creada con Éxito!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tu cuenta ha sido registrada correctamente. Puedes iniciar sesión.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(onClick = { showSuccess = false; onBackToLogin() }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))) {
                                Text("Volver al Inicio", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    IntegradoraEventNodeTheme {
        RegisterScreen()
    }
}
