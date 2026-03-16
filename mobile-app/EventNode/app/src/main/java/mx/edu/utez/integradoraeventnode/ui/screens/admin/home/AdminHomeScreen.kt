package mx.edu.utez.integradoraeventnode.ui.screens.admin.home

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
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

@Composable
fun AdminHomeScreen(
    modifier: Modifier = Modifier,
    onViewEventDetail: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    
    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Search Bar
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar eventos o usuarios", color = Color(0xFF999999)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Image(
                            bitmap = assetImageBitmap("home.png"), // Lupa placeholder
                            contentDescription = null,
                            modifier = Modifier.size(20.dp).padding(start = 8.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdminStatCard(
                        title = "Certificaciones\nEmitidas",
                        count = "2",
                        icon = "diploma.png",
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Usuarios Registrados",
                        count = "10",
                        icon = "user.png",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Eventos Próximos
                SectionHeader(title = "Eventos Próximos", action = "Ver todos")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminEventCard(
                        day = "15",
                        month = "OCT",
                        title = "Conferencia de Innovación",
                        location = "Auditorio Principal",
                        status = "ACTIVO",
                        statusColor = Color(0xFF4CAF50),
                        onClick = onViewEventDetail
                    )
                    AdminEventCard(
                        day = "02",
                        month = "NOV",
                        title = "Taller de Liderazgo",
                        location = "Sala de Grados",
                        status = "PENDIENTE",
                        statusColor = Color(0xFFFFB300),
                        onClick = onViewEventDetail
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Certificaciones Recientes
                SectionHeader(title = "Certificaciones Recientes", action = "Gestionar")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        RecentCertItem(
                            title = "Diplomado en Gestión Pública",
                            info = "EMITIDO: 20 SEP 2023",
                            status = "COMPLETADO",
                            statusColor = Color(0xFF4CAF50),
                            icon = "diploma.png"
                        )
                        Divider(color = Color(0xFFF0F0F0))
                        RecentCertItem(
                            title = "Seminario de Tecnología",
                            info = "FECHA: 12 OCT 2023",
                            status = "PENDIENTE",
                            statusColor = Color(0xFFFFB300),
                            icon = "diploma.png"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bottom Nav
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AdminBottomNav(
                    selected = "Inicio",
                    onHome = {},
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun AdminStatCard(title: String, count: String, icon: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0F7FF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap(icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color(0xFF7A7A7A), lineHeight = 14.sp)
                Text(text = count, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AdminEventCard(
    day: String,
    month: String,
    title: String,
    location: String,
    status: String,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(45.dp)
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = month, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = day, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = status, style = MaterialTheme.typography.labelSmall, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            
            Text("〉", color = Color.LightGray, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
private fun RecentCertItem(title: String, info: String, status: String, statusColor: Color, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F6FA)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = assetImageBitmap(icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = info, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 9.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "•", color = Color.LightGray)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = status, style = MaterialTheme.typography.labelSmall, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 9.sp)
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = action, style = MaterialTheme.typography.labelSmall, color = Color(0xFF2F6FED), fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminHomeScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminHomeScreen()
    }
}
