package mx.edu.utez.integradoraeventnode.ui.screens.student.profile

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

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
    var password by remember { mutableStateOf("********") }

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
                        text = "Panfila Portillo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "DESARROLLO DE SOFTWARE",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF2F6FED),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "panfila.portilla@eventnode.edu",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A7A7A)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF0F2F5))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ID: 2024098231",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF555555),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Personal Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Datos del Alumno",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InfoRow(label = "Nombre", value = "Panfila", icon = "user.png")
                    InfoRow(label = "Apellidos", value = "Portillo", icon = "user.png")
                    InfoRow(label = "Correo", value = "panfila.portillo@eventnode.edu", icon = "correo.png")
                    InfoRow(label = "Matrícula", value = "2024098231", icon = "diploma.png")
                    InfoRow(label = "Sexo", value = "Femenino", icon = "user.png")
                    InfoRow(label = "Cuatrimestre", value = "7mo", icon = "book-open-reader.png")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Change Password Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Cambiar contraseña",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "CONTRASEÑA ACTUAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF7A7A7A),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8F9FB),
                                unfocusedContainerColor = Color(0xFFF8F9FB),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            leadingIcon = {
                                Image(
                                    bitmap = assetImageBitmap("lock.png"),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(Color.Gray)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

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
                    Text("Cerrar Sesion", style = MaterialTheme.typography.titleMedium, color = Color.White)
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
fun ProfileBottomNav(onHome: () -> Unit, onAgenda: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(label = "Inicio", icon = "home.png", selected = false, onClick = onHome)
            BottomNavItem(label = "Agenda", icon = "book-open-reader.png", selected = false, onClick = onAgenda)
            BottomNavItem(label = "Diplomas", icon = "diploma.png", selected = false, onClick = onDiplomas)
            BottomNavItem(label = "Perfil", icon = "user.png", selected = true, onClick = onProfile)
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    IntegradoraEventNodeTheme {
        ProfileScreen()
    }
}
