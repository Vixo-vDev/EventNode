package mx.edu.utez.integradoraeventnode.ui.screens.student.agenda

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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun StudentEventDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onCancelSuccess: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 90.dp)
            ) {
                // Image Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFF7FA7C6)) // Placeholder for image
                ) {
                    // Back button
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = assetImageBitmap("arrow-small-left.png"),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF333333))
                        )
                    }
                }

                // Event Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Festival en el campus",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE6F0FF))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "CULTURA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2F6FED),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        DetailItem(
                            label = "FECHA Y HORA",
                            value = "Viernes, 24 Octubre - 16:00 - 20:00",
                            icon = "chart-histogram.png"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        DetailItem(
                            label = "UBICACIÓN",
                            value = "Auditorio Principal,\nEdificio Central",
                            icon = "book-open-reader.png"
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Información del Evento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Únete a nosotros en la celebración anual del campus. Disfrutaremos de música en vivo, stands de comida local y presentaciones de los clubes universitarios. Un espacio perfecto para conectar con otros estudiantes y disfrutar de la vida universitaria fuera de las aulas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            lineHeight = 20.sp
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                        ) {
                            Text(
                                "Cancelar inscripción",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
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
                StudentBottomNav(selected = "Agenda", onHome = onHome, onAgenda = onAgenda, onDiplomas = onDiplomas, onProfile = onProfile)
            }

            // Cancel Confirmation Dialog
            if (showCancelDialog) {
                DialogOverlay {
                    CancelDialog(
                        onConfirm = {
                            showCancelDialog = false
                            showSuccessDialog = true
                        },
                        onDismiss = { showCancelDialog = false }
                    )
                }
            }

            // Success Dialog
            if (showSuccessDialog) {
                DialogOverlay {
                    SuccessDialog(
                        onHome = onHome,
                        onOtherEvents = {
                            showSuccessDialog = false
                            onHome()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, icon: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F7FF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = assetImageBitmap(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
private fun DialogOverlay(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CancelDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .widthIn(max = 340.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    bitmap = assetImageBitmap("chart-histogram.png"),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "¿Estás seguro de que deseas cancelar tu registro?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Esta acción eliminará lógicamente tu registro. Ten en cuenta que puedes cancelar tu registro dentro de los 30 minutos posteriores a haberte registrado.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
            ) {
                Text("Confirmar cancelación", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
            ) {
                Text("Mantener registro", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SuccessDialog(onHome: () -> Unit, onOtherEvents: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .widthIn(max = 340.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2F6FED)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 40.sp, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "¡Cancelación exitosa!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tu registro al evento ha sido eliminado correctamente de nuestra base de datos. Lamentamos que no puedas asistir.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
            ) {
                Text("Volver al Inicio", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onOtherEvents,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
            ) {
                Text("Ver otros eventos", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StudentBottomNav(selected: String, onHome: () -> Unit, onAgenda: () -> Unit, onDiplomas: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(
                label = "Inicio",
                icon = "home.png",
                selected = selected == "Inicio",
                onClick = onHome
            )
            BottomNavItem(
                label = "Agenda",
                icon = "book-open-reader.png",
                selected = selected == "Agenda",
                onClick = onAgenda
            )
            BottomNavItem(
                label = "Diplomas",
                icon = "diploma.png",
                selected = selected == "Diplomas",
                onClick = onDiplomas
            )
            BottomNavItem(
                label = "Perfil",
                icon = "user.png",
                selected = selected == "Perfil",
                onClick = onProfile
            )
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B)
    val tint = ColorFilter.tint(if (selected) Color(0xFF2F6FED) else Color(0xFF8B8B8B))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            bitmap = assetImageBitmap(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            colorFilter = tint
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun StudentEventDetailScreenPreview() {
    IntegradoraEventNodeTheme {
        StudentEventDetailScreen()
    }
}
