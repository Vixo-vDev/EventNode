package mx.edu.utez.integradoraeventnode.ui.screens.admin.profile

import android.graphics.BitmapFactory
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.content.Context
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProfileScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf(prefs.getString("nombre", "") ?: "") }
    var lastName by remember { mutableStateOf("${prefs.getString("apellidoPaterno", "") ?: ""} ${prefs.getString("apellidoMaterno", "") ?: ""}".trim()) }
    var email by remember { mutableStateOf(prefs.getString("correo", "") ?: "") }
    var id by remember { mutableStateOf(prefs.getString("matricula", "") ?: "") }
    var gender by remember { mutableStateOf(if (prefs.getString("sexo", "") == "M") "Masculino" else if (prefs.getString("sexo", "") == "F") "Femenino" else "Otro") }
    var quarter by remember { mutableStateOf("${prefs.getString("cuatrimestre", "") ?: ""}°") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    var showSuccessDialog by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image with Edit Badge
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            bitmap = assetImageBitmap("user.png"), // Placeholder para Alejandro
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
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Form Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            EditField(label = "NOMBRE", value = name, onValueChange = { name = it }, icon = "user.png")
                            EditField(label = "APELLIDOS", value = lastName, onValueChange = { lastName = it }, icon = "user.png")
                            EditField(label = "CORREO", value = email, onValueChange = { email = it }, icon = "correo.png")
                            EditField(label = "MATRÍCULA", value = id, onValueChange = { id = it }, icon = "diploma.png")
                            
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Cambiar contraseña", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            EditField(
                                label = "CONTRASEÑA ACTUAL", 
                                value = currentPassword, 
                                onValueChange = { currentPassword = it },
                                isPassword = true,
                                passwordVisible = currentPasswordVisible,
                                onPasswordToggle = { currentPasswordVisible = !currentPasswordVisible },
                                icon = "lock.png"
                            )
                            EditField(
                                label = "NUEVA CONTRASEÑA", 
                                value = newPassword, 
                                onValueChange = { newPassword = it },
                                isPassword = true,
                                passwordVisible = newPasswordVisible,
                                onPasswordToggle = { newPasswordVisible = !newPasswordVisible },
                                icon = "lock.png"
                            )
                            EditField(
                                label = "CONFIRMAR NUEVA CONTRASEÑA", 
                                value = confirmPassword, 
                                onValueChange = { confirmPassword = it },
                                isPassword = true,
                                passwordVisible = confirmPasswordVisible,
                                onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                                icon = "lock.png"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { showSuccessDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                    ) {
                        Text("Guardar datos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                AdminBottomNav(
                    selected = "Perfil",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }

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
        Text(text = label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
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
                    } else null,
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
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
                } else null,
                trailingIcon = if (isPassword) {
                    {
                        IconButton(onClick = onPasswordToggle) {
                            val iconName = if (passwordVisible) "vista.png" else "eye.png"
                            Image(
                                bitmap = assetImageBitmap(iconName),
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else null
            )
        }
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
                Text(text = "¡Datos actualizados!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu información ha sido guardada correctamente en nuestro sistema. Los cambios ya son visibles en tu perfil público.",
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

@Preview(showBackground = true)
@Composable
fun AdminEditProfileScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminEditProfileScreen()
    }
}