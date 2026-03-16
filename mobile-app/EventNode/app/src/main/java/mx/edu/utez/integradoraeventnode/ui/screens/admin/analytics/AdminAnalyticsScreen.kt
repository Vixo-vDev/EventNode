package mx.edu.utez.integradoraeventnode.ui.screens.admin.analytics

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

@Composable
fun AdminAnalyticsScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(20.dp)) {
                    // Rendimiento del evento
                    Text(
                        text = "Rendimiento del evento",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Tasa de asistencia a lo largo del tiempo", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "85%", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFE8F5E9))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "↗ +5%", color = Color(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            // Simulación de gráfica de barras
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                val heights = listOf(0.3f, 0.4f, 0.25f, 0.45f, 0.35f, 0.6f, 0.55f)
                                heights.forEachIndexed { index, height ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(height)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(if (index == 5) Color(0xFF2F6FED) else Color(0xFFBBDEFB))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Categorías de eventos populares
                    Text(
                        text = "Categorías de eventos populares",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            CategoryProgressItem("Charlas tecnológicas", 0.8f, "+10%", Color(0xFF2F6FED))
                            CategoryProgressItem("Talleres", 0.72f, "72%", Color(0xFF5C9DFF))
                            CategoryProgressItem("Seminarios", 0.45f, "45%", Color(0xFFBBDEFB))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Desglose demográfico
                    Text(
                        text = "Desglose demográfico",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Estudiantes por segmento", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(24.dp))
                            // Placeholder para gráfica circular o similar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    DemographicBar("66%", "1ER AÑO", Color(0xFFE3F2FD))
                                    DemographicBar("82%", "> 21 AÑOS", Color(0xFF2F6FED))
                                    DemographicBar("42%", "TIEMPO C.", Color(0xFFBBDEFB))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Autoidentificación migrante
                    Text(
                        text = "Autoidentificación migrante",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(Color(0xFFE3F2FD))
                            ) {
                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.25f).background(Color(0xFF2F6FED)))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF2F6FED)))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "MIGRANTES", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    }
                                    Text(text = "25%", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFBBDEFB)))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "NO MIGRANTES", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    }
                                    Text(text = "75%", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
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
                    selected = "Analitica",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = {},
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun CategoryProgressItem(label: String, progress: Float, value: String, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (value.contains("+")) Color(0xFF2F6FED) else Color.Black)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF5F6FA)
        )
    }
}

@Composable
private fun DemographicBar(percentage: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(text = percentage, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (percentage == "82%") Color.White else Color(0xFF2F6FED))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAnalyticsScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminAnalyticsScreen()
    }
}