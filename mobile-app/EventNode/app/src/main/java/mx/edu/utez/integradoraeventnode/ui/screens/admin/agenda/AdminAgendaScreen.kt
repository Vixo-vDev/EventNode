package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import android.graphics.BitmapFactory
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
fun AdminAgendaScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {},
    onViewDetail: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Próximos, 1: Pasados

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                // Tabs
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TabItem(
                            text = "Próximos",
                            selected = selectedTab == 0,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = 0 }
                        )
                        TabItem(
                            text = "Pasados",
                            selected = selectedTab == 1,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = 1 }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    if (selectedTab == 0) {
                        Text(
                            text = "Hoy, 24 de Octubre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        AdminAgendaCard(
                            image = "Gemini_Generated_Image_j7p5usj7p5usj7p5.png",
                            title = "Charla tecnológica: IA en el campus",
                            time = "10:00 AM - 12:30 PM",
                            location = "Auditorio Principal, Campus Central",
                            badge = "EN VIVO",
                            badgeColor = Color(0xFFE3F2FD),
                            badgeTextColor = Color(0xFF2196F3),
                            onDetail = onViewDetail
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        AdminAgendaCard(
                            image = "Gemini_Generated_Image_8wktbs8wktbs8wkt.png",
                            title = "Workshop: Desarrollo Web Sostenible",
                            time = "03:00 PM - 05:00 PM",
                            location = "Laboratorio de Cómputo B2",
                            onDetail = onViewDetail
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Mañana, 25 de Octubre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Card compacta como en la imagen
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF5F6FA)),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "25", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2F6FED))
                                    Text(text = "OCT", fontSize = 12.sp, color = Color(0xFF2F6FED))
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Feria de Empleo 2024", fontWeight = FontWeight.Bold)
                                    Text(text = "9:00 AM - Explanada Norte", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = "Próximamente", fontSize = 11.sp, color = Color.LightGray)
                                }
                                
                                TextButton(onClick = onViewDetail) {
                                    Text("Ver detalles", fontSize = 12.sp, color = Color(0xFF2F6FED))
                                }
                            }
                        }
                    } else {
                        // Lista de eventos pasados (placeholder)
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay eventos pasados", color = Color.Gray)
                        }
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
                    selected = "Agenda",
                    onHome = onHome,
                    onAgenda = {},
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
private fun TabItem(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFF2F6FED) else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(2.dp)
                .background(if (selected) Color(0xFF2F6FED) else Color.Transparent)
        )
    }
}

@Composable
private fun AdminAgendaCard(
    image: String,
    title: String,
    time: String,
    location: String,
    badge: String? = null,
    badgeColor: Color = Color.Transparent,
    badgeTextColor: Color = Color.Transparent,
    onDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    bitmap = assetImageBitmap(image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                if (badge != null) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd),
                        color = badgeColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = badgeTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕒", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDetail,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Ver detalles")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAgendaScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminAgendaScreen()
    }
}