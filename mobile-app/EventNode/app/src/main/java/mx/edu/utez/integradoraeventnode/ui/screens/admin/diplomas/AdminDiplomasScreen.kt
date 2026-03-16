package mx.edu.utez.integradoraeventnode.ui.screens.admin.diplomas

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
import androidx.compose.ui.graphics.graphicsLayer
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

enum class DiplomaAdminState {
    MENU,
    BUILDER,
    GENERATOR
}

@Composable
fun AdminDiplomasScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var currentState by remember { mutableStateOf(DiplomaAdminState.MENU) }
    var selectedTemplate by remember { mutableStateOf(0) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (currentState) {
                    DiplomaAdminState.MENU -> DiplomasMenuView(
                        onBuilder = { currentState = DiplomaAdminState.BUILDER },
                        onGenerator = { currentState = DiplomaAdminState.GENERATOR }
                    )
                    DiplomaAdminState.BUILDER -> ConstructorPlantillasView(
                        selectedTemplate = selectedTemplate,
                        onTemplateSelect = { selectedTemplate = it },
                        onNext = { currentState = DiplomaAdminState.GENERATOR },
                        onBack = { currentState = DiplomaAdminState.MENU }
                    )
                    DiplomaAdminState.GENERATOR -> GenerarDiplomasView(
                        selectedTemplate = selectedTemplate,
                        onTemplateSelect = { selectedTemplate = it },
                        onBack = { currentState = DiplomaAdminState.MENU },
                        onGenerate = { showSuccessDialog = true }
                    )
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
                    selected = "Diplomas",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = {},
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }

    if (showSuccessDialog) {
        DiplomaSuccessDialog(
            onDismiss = { 
                showSuccessDialog = false
                currentState = DiplomaAdminState.MENU 
            },
            onAnother = {
                showSuccessDialog = false
                currentState = DiplomaAdminState.GENERATOR
            }
        )
    }
}

@Composable
private fun DiplomaSuccessDialog(onDismiss: () -> Unit, onAnother: () -> Unit) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
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
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color(0xFF4CAF50), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¡Diplomas Generados!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Los certificados han sido creados exitosamente para todos los alumnos seleccionados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Volver al Menú", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onAnother) {
                    Text("Generar otros", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun DiplomasMenuView(onBuilder: () -> Unit, onGenerator: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gestión de Diplomas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        MenuOptionCard(
            title = "Constructor de Plantillas",
            description = "Crea y personaliza nuevos diseños para tus certificados.",
            icon = "diploma.png",
            onClick = onBuilder
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuOptionCard(
            title = "Generar Diplomas",
            description = "Emite certificados masivos para los alumnos registrados.",
            icon = "user.png",
            onClick = onGenerator
        )
    }
}

@Composable
private fun MenuOptionCard(title: String, description: String, icon: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F7FF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap(icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Image(
                bitmap = assetImageBitmap("arrow-small-left.png"),
                contentDescription = null,
                modifier = Modifier.size(24.dp).graphicsLayer(rotationZ = 180f),
                colorFilter = ColorFilter.tint(Color.LightGray)
            )
        }
    }
}

@Composable
private fun ConstructorPlantillasView(
    selectedTemplate: Int,
    onTemplateSelect: (Int) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Image(bitmap = assetImageBitmap("arrow-small-left.png"), contentDescription = "Volver", colorFilter = ColorFilter.tint(Color.Black))
            }
            Text(
                text = "Constructor de Plantillas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // To balance the back button
        }

        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            color = Color(0xFFE3F2FD),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🛠️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Estás en el modo de diseño. Aquí creas la base para futuros diplomas.", fontSize = 11.sp, color = Color(0xFF1976D2))
            }
        }

        Text(text = "Seleccionar una plantilla", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TemplateItem(
                image = "diploma.png",
                label = "Plantilla 1",
                selected = selectedTemplate == 0,
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelect(0) }
            )
            TemplateItem(
                image = "diploma.png",
                label = "Plantilla 2",
                selected = selectedTemplate == 1,
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelect(1) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Personalizar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        CustomOptionItem(icon = "home.png", label = "Subir firma")
        Spacer(modifier = Modifier.height(8.dp))
        CustomOptionItem(icon = "home.png", label = "Ubicación del logo")
        Spacer(modifier = Modifier.height(8.dp))
        CustomOptionItem(icon = "home.png", label = "Selección de fondo")

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Avance", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "Vista previa del certificado", color = Color(0xFF2F6FED), fontSize = 11.sp)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎓", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "CERTIFICADO DE PARTICIPACIÓN", fontSize = 10.sp, color = Color.Gray)
                Text(text = "otorgado con orgullo a:", fontSize = 8.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Alex Turner", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Por completar satisfactoriamente el evento:", fontSize = 8.sp, color = Color.Gray)
                Text(text = "Leadership Workshop", color = Color(0xFF2F6FED), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                Divider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(bitmap = assetImageBitmap("user.png"), contentDescription = null, modifier = Modifier.size(12.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Nombre del estudiante: Alex Turner", fontSize = 9.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(bitmap = assetImageBitmap("home.png"), contentDescription = null, modifier = Modifier.size(12.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Título del evento: Leadership Workshop", fontSize = 9.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(bitmap = assetImageBitmap("diploma.png"), contentDescription = null, modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar plantilla", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GenerarDiplomasView(
    selectedTemplate: Int,
    onTemplateSelect: (Int) -> Unit,
    onBack: () -> Unit,
    onGenerate: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Image(bitmap = assetImageBitmap("arrow-small-left.png"), contentDescription = "Volver", colorFilter = ColorFilter.tint(Color.Black))
            }
            Text(
                text = "Generar Diplomas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            color = Color(0xFFF1F8E9),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📄", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Estás en el modo de generación. Emite certificados para tus alumnos.", fontSize = 11.sp, color = Color(0xFF388E3C))
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Seleccionar una plantilla", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "Ver todos", color = Color(0xFF2F6FED), fontSize = 12.sp, modifier = Modifier.clickable { onBack() })
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TemplateItem(
                image = "diploma.png",
                label = "Plantilla Clásica",
                selected = selectedTemplate == 0,
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelect(0) }
            )
            TemplateItem(
                image = "diploma.png",
                label = "Plantilla Moderna",
                selected = selectedTemplate == 1,
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelect(1) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Lista de Estudiantes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        StudentGenerarItem(name = "Alejandro García", id = "ID: #EV-2024-001", status = "LISTO", statusColor = Color(0xFFE8F5E9), textColor = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(12.dp))
        StudentGenerarItem(name = "Maria Fernández", id = "ID: #EV-2024-002", status = "PENDIENTE", statusColor = Color(0xFFFFF3E0), textColor = Color(0xFFFF9800))

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Personalizar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        CustomOptionItem(icon = "home.png", label = "Subir firma")
        Spacer(modifier = Modifier.height(8.dp))
        CustomOptionItem(icon = "home.png", label = "Ubicación del logo")
        Spacer(modifier = Modifier.height(8.dp))
        CustomOptionItem(icon = "home.png", label = "Selección de fondo")

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onGenerate,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(bitmap = assetImageBitmap("diploma.png"), contentDescription = null, modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generar Diplomas", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TemplateItem(image: String, label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = if (selected) Color(0xFF2F6FED) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(Color.White)
        ) {
            Image(
                bitmap = assetImageBitmap(image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Fit
            )
            if (selected) {
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(16.dp).clip(CircleShape).background(Color(0xFF2F6FED)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 10.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 11.sp, color = if (selected) Color(0xFF2F6FED) else Color.Gray, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun CustomOptionItem(icon: String, label: String) {
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
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF0F7FF)),
                contentAlignment = Alignment.Center
            ) {
                Image(bitmap = assetImageBitmap(icon), contentDescription = null, modifier = Modifier.size(18.dp), colorFilter = ColorFilter.tint(Color(0xFF2F6FED)))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text("〉", color = Color.LightGray)
        }
    }
}

@Composable
private fun StudentGenerarItem(name: String, id: String, status: String, statusColor: Color, textColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = id, fontSize = 11.sp, color = Color.Gray)
            }
            Surface(
                color = statusColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDiplomasScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminDiplomasScreen()
    }
}