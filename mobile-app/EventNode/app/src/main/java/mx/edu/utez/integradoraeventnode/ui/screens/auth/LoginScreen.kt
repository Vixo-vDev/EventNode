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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.LoginRequest
import org.json.JSONObject
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateAccount: () -> Unit = {},
    onLogin: (isAdmin: Boolean) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepSession by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showRecoverPassword by remember { mutableStateOf(false) }
    var showCodeModal by remember { mutableStateOf(false) }
    var showEmailSent by remember { mutableStateOf(false) }
    var recoverEmail by remember { mutableStateOf("") }
    var recoverCode by remember { mutableStateOf("") }
    var showSuccessModal by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("EventNodePrefs", android.content.Context.MODE_PRIVATE) }
    var attempts by remember { mutableIntStateOf(prefs.getInt("loginAttempts", 0)) }
    var lockoutUntil by remember { mutableLongStateOf(prefs.getLong("lockoutUntil", 0L)) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
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
                                .height(120.dp)
                                .padding(vertical = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Disfrute los eventos que tenemos preparados para ti",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF74777F),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )
                        }
                        
                        Text(
                            text = "Correo Institucional",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44474E),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
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
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Contraseña",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF44474E)
                            )
                            Text(
                                text = "¿Haz olvidado la contraseña?",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF2F6FED),
                                modifier = Modifier.clickable { showRecoverPassword = true }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
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
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = keepSession,
                                onCheckedChange = { keepSession = it }
                            )
                            Text(
                                text = "Mantener Sesión Iniciada",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF44474E)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                val now = System.currentTimeMillis()
                                if (lockoutUntil > now) {
                                    val remaining = (lockoutUntil - now) / 60000 + 1
                                    errorMessage = "Cuenta bloqueada por seguridad. Intente en $remaining minutos."
                                    return@Button
                                } else if (lockoutUntil > 0L) {
                                    attempts = 0
                                    lockoutUntil = 0L
                                    prefs.edit().putInt("loginAttempts", 0).putLong("lockoutUntil", 0L).apply()
                                }

                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Credenciales incorrectas"
                                    return@Button
                                }

                                isLoading = true
                                errorMessage = null
                                
                                scope.launch {
                                    try {
                                        val response = ApiClient.apiService.login(LoginRequest(email, password))
                                        if (response.isSuccessful) {
                                            val body = response.body()
                                            val rol = body?.rol ?: ""
                                            prefs.edit().apply {
                                                putInt("loginAttempts", 0)
                                                putString("nombre", body?.nombre ?: "")
                                                putString("apellidoPaterno", body?.apellidoPaterno ?: "")
                                                putString("apellidoMaterno", body?.apellidoMaterno ?: "")
                                                putString("correo", body?.correo ?: "")
                                                putString("matricula", body?.matricula ?: "")
                                                putString("sexo", body?.sexo ?: "")
                                                putInt("cuatrimestre", body?.cuatrimestre ?: 0)
                                                putString("rol", rol)
                                                putBoolean("mantenerSesion", keepSession)
                                            }.apply()
                                            attempts = 0
                                            showSuccessModal = true
                                            kotlinx.coroutines.delay(1500)
                                            showSuccessModal = false
                                            onLogin(rol.contains("ADMIN", ignoreCase = true))
                                        } else {
                                            attempts++
                                            prefs.edit().putInt("loginAttempts", attempts).apply()
                                            
                                            val errorStr = response.errorBody()?.string()
                                            var msg = "Credenciales incorrectas"
                                            if (errorStr != null) {
                                                try {
                                                    val json = JSONObject(errorStr)
                                                    msg = json.optString("mensaje", "Credenciales incorrectas")
                                                } catch(e: Exception) {}
                                            }
                                            
                                            if (attempts >= 3) {
                                                val lockTime = System.currentTimeMillis() + 15 * 60 * 1000
                                                lockoutUntil = lockTime
                                                prefs.edit().putLong("lockoutUntil", lockTime).apply()
                                                errorMessage = "Ha fallado 3 veces. El sistema bloquea el acceso por 15 minutos."
                                            } else {
                                                errorMessage = msg
                                            }
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error de conexión"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                        ) {
                            Text(if (isLoading) "Cargando..." else "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "¿No tienes una cuenta?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF44474E)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Crea una cuenta",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2F6FED),
                                modifier = Modifier.clickable { onCreateAccount() }
                            )
                        }
                    }
                }
            }

            if (showSuccessModal) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0x80000000)).clickable(enabled = false) {}, contentAlignment = Alignment.Center) {
                    Card(modifier = Modifier.fillMaxWidth().padding(24.dp).widthIn(max = 360.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                                Image(bitmap = assetImageBitmap("check-circle.png"), contentDescription = null, modifier = Modifier.size(32.dp), colorFilter = ColorFilter.tint(Color(0xFF4CAF50)))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("¡Inicio de sesión exitoso!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Conectando con el servidor...", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666), textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            if (showRecoverPassword) {
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
                                    .background(Color(0xFFF0F7FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("eye.png"), // Using eye icon as in Image 3
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "Recuperar contraseña",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Ingresa tu correo institucional para enviarte las instrucciones de recuperación.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "Correo institucional",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = recoverEmail,
                                onValueChange = { recoverEmail = it },
                                placeholder = { Text("matricula@utez.edu.mx", color = Color(0xFF999999)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Image(
                                        bitmap = assetImageBitmap("correo.png"),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFF74777F))
                                    )
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = {
                                    showRecoverPassword = false
                                    showEmailSent = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                            ) {
                                Text("Enviar código", fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Volver al inicio de sesión",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF666666),
                                modifier = Modifier.clickable { showRecoverPassword = false }
                            )
                        }
                    }
                }
            }

            if (showEmailSent) {
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
                                    .background(Color(0xFFF0F7FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = assetImageBitmap("envelope.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "¡Correo Enviado!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Hemos enviado un enlace de recuperación a tu correo institucional. Por favor, revisa tu bandeja de entrada y spam.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Button(
                                onClick = { showEmailSent = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                            ) {
                                Text("Volver al Inicio", fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("¿No recibiste nada?", style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reenviar correo",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2F6FED),
                                    modifier = Modifier.clickable { }
                                )
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
fun LoginScreenPreview() {
    IntegradoraEventNodeTheme {
        LoginScreen()
    }
}
