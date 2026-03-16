package mx.edu.utez.integradoraeventnode.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var enrollment by remember { mutableStateOf("") }
    var institutionalEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var genderIdentity by remember { mutableStateOf("Hombre") }
    var showPassword by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

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
                        
                        Text(
                            text = "Nombre(s)*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Ingresa tu nombre", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Apellidos*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            placeholder = { Text("Ingresa tus apellidos", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Matrícula*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = enrollment,
                            onValueChange = { enrollment = it },
                            placeholder = { Text("Ej: 20243ds01", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Correo institucional*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = institutionalEmail,
                            onValueChange = { institutionalEmail = it },
                            placeholder = { Text("matricula@utez.edu.mx", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            ),
                            leadingIcon = {
                                Image(
                                    bitmap = assetImageBitmap("correo.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Contraseña*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("********", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            ),
                            leadingIcon = {
                                Image(
                                    bitmap = assetImageBitmap("lock.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    val iconName = if (showPassword) "vista.png" else "eye.png"
                                    Image(
                                        bitmap = assetImageBitmap(iconName),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Requisitos: mínimo 8 caracteres, mayúsculas, minúsculas, números y símbolos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Edad*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            placeholder = { Text("18", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Sexo*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = sex,
                            onValueChange = { sex = it },
                            placeholder = { Text("Seleccionar", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                Image(
                                    bitmap = assetImageBitmap("arrow-small-left.png"), // Rotated to look like dropdown
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp).graphicsLayer(rotationZ = -90f),
                                    colorFilter = ColorFilter.tint(Color(0xFF74777F))
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Cuatrimestre*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = semester,
                            onValueChange = { semester = it },
                            placeholder = { Text("1", color = Color(0xFF999999)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color(0xFFE1E2EC),
                                unfocusedIndicatorColor = Color(0xFFE1E2EC)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = "Identidad de Género*",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = genderIdentity == "Hombre",
                                    onClick = { genderIdentity = "Hombre" },
                                    colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = Color(0xFF2F6FED))
                                )
                                Text("Hombre", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = genderIdentity == "Mujer",
                                    onClick = { genderIdentity = "Mujer" },
                                    colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = Color(0xFF2F6FED))
                                )
                                Text("Mujer", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = genderIdentity == "Otro",
                                    onClick = { genderIdentity = "Otro" },
                                    colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = Color(0xFF2F6FED))
                                )
                                Text("Otro", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44474E))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { showSuccess = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                        ) {
                            Text("Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "¿Ya tienes una cuenta?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF44474E)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Inicia sesión",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2F6FED),
                                modifier = Modifier.clickable { onBackToLogin() }
                            )
                        }
                    }
                }
            }

            if (showSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .widthIn(max = 360.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("check-circle.png"), // Assuming check-circle.png exists or using placeholder
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF4CAF50))
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "¡Cuenta Creada con Éxito!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Tu cuenta institucional ha sido registrada correctamente. Ya puedes comenzar a explorar y participar en los eventos de tu universidad.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Button(
                                onClick = {
                                    showSuccess = false
                                    onBackToLogin()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                            ) {
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
