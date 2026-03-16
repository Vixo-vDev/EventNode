package mx.edu.utez.integradoraeventnode.ui.screens.student.diplomas

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.screens.student.profile.ProfileBottomNav

@Composable
fun DiplomasScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val diplomas = listOf(
        DiplomaData("Introducción a la ciencia de datos", "12 Oct 2023", Color(0xFF6F9EA6)),
        DiplomaData("Desarrollo Web Fullstack", "05 Sep 2023", Color(0xFFD1B0A0)),
        DiplomaData("Diseño UX/UI Avanzado", "20 Ago 2023", Color(0xFFE5D1B8))
    )

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                if (diplomas.isEmpty()) {
                    EmptyDiplomasState(onExplore = onHome)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Tus diplomas ganados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Has completado ${diplomas.size} cursos este semestre",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF7A7A7A)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        diplomas.forEach { diploma ->
                            DiplomaCard(diploma)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ProfileBottomNav(
                    currentScreen = "Diplomas",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onDiplomas = {},
                    onProfile = onProfile
                )
            }
        }
    }
}

data class DiplomaData(val title: String, val date: String, val color: Color)

@Composable
private fun EmptyDiplomasState(onExplore: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 340.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = assetImageBitmap("Gemini_Generated_Image_lf9n0ilf9n0ilf9n.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Aún no hay\ndiplomas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No has completado ningún evento todavía. Explora eventos para comenzar a obtener certificados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7A7A7A),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onExplore,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Explorar eventos", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DiplomaCard(diploma: DiplomaData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(diploma.color.copy(alpha = 0.8f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for the certificate image
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(120.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("CERTIFICATE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("FOR COMPLETION", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(40.dp).height(1.dp).background(Color.LightGray))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = diploma.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Terminado",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• Emisión: ${diploma.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A7A7A)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Image(
                        bitmap = assetImageBitmap("qr-scan.png"), // Reusing download-like icon if exists or similar
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Descargar PDF", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun DiplomasBottomNav(onHome: () -> Unit, onAgenda: () -> Unit, onProfile: () -> Unit) {
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
            BottomNavItem(label = "Diplomas", icon = "diploma.png", selected = true, onClick = {})
            BottomNavItem(label = "Perfil", icon = "user.png", selected = false, onClick = onProfile)
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
fun DiplomasScreenPreview() {
    IntegradoraEventNodeTheme {
        DiplomasScreen()
    }
}
