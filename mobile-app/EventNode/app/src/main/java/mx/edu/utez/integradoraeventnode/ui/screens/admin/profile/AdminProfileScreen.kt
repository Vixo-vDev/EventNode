package mx.edu.utez.integradoraeventnode.ui.screens.admin.profile

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

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Profile Section (Synchronized with Student)
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
                                .background(Color(0xFFD9D9D9)),
                            contentScale = ContentScale.Crop
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
                        text = "Panfila Portillo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "20243ds088@utez.edu.mx",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2F6FED)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Desarrollo de Software",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Matrícula: 20243ds088",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Datos del Alumno Section
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(text = "Datos del Administrador", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ProfileInfoItem(label = "Nacionalidad:", value = "Mexicana", icon = "user.png")
                    ProfileInfoItem(label = "Idioma:", value = "Español", icon = "user.png")
                    ProfileInfoItem(label = "Correo:", value = "20243ds088@utez.edu.mx", icon = "correo.png")
                    ProfileInfoItem(label = "Género:", value = "Femenino", icon = "user.png")

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "Certificados emitidos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    CertificadoItem(date = "15-05-2023", title = "Festival en el Campus")
                    Spacer(modifier = Modifier.height(12.dp))
                    CertificadoItem(date = "20-10-2023", title = "charla tecnológica")

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