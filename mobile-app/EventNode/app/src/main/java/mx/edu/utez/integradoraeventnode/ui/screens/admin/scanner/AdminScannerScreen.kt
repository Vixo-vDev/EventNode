package mx.edu.utez.integradoraeventnode.ui.screens.admin.scanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

enum class ScannerState {
    INITIAL,
    SCANNING,
    SUCCESS,
    FAILURE,
    MANUAL
}

@Composable
fun AdminScannerScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    var scannerState by remember { mutableStateOf(ScannerState.INITIAL) }
    var manualId by remember { mutableStateOf("") }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                when (scannerState) {
                    ScannerState.INITIAL -> InitialScannerView(onScan = { scannerState = ScannerState.SCANNING }, onManual = { scannerState = ScannerState.MANUAL })
                    ScannerState.SCANNING -> ScanningView(onBack = { scannerState = ScannerState.INITIAL }, onScanSuccess = { scannerState = ScannerState.SUCCESS }, onScanFailure = { scannerState = ScannerState.FAILURE })
                    ScannerState.SUCCESS -> SuccessView(onConfirm = { scannerState = ScannerState.INITIAL })
                    ScannerState.FAILURE -> FailureView(onRetry = { scannerState = ScannerState.SCANNING }, onManual = { scannerState = ScannerState.MANUAL })
                    ScannerState.MANUAL -> ManualRegistrationView(
                        manualId = manualId,
                        onIdChange = { manualId = it },
                        onBack = { scannerState = ScannerState.INITIAL },
                        onRegisterSuccess = { scannerState = ScannerState.SUCCESS }
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
                    selected = "Escanear",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = {},
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }
}

@Composable
private fun InitialScannerView(onScan: () -> Unit, onManual: () -> Unit) {
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
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Escáner QR para\nAdministrador",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFF5F6FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = assetImageBitmap("qr-scan.png"),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Escanea el código QR del alumno para registrar su asistencia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Button(
            onClick = onManual,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
        ) {
            Text("Entrada manual", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        // Button to simulate scan for testing
        TextButton(onClick = onScan, modifier = Modifier.padding(top = 16.dp)) {
            Text("Simular Escaneo", color = Color.Gray)
        }
    }
}

@Composable
private fun ScanningView(onBack: () -> Unit, onScanSuccess: () -> Unit, onScanFailure: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Escanea el código QR",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )
        Text(
            text = "Ubica el código dentro del recuadro para registrar asistencia",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(32.dp))
        ) {
            // Camera placeholder
            Image(
                bitmap = assetImageBitmap("Gemini_Generated_Image_j7p5usj7p5usj7p5.png"), // Placeholder for camera view
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Scanner frame overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("qr-scan.png"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onScanSuccess, // Simulate success
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
        ) {
            Text("Escanear", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        TextButton(onClick = onScanFailure, modifier = Modifier.padding(top = 8.dp)) {
            Text("Simular Fallo", color = Color.Gray)
        }
    }
}

@Composable
private fun SuccessView(onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 32.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "¡Escaneo Exitoso!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Student Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F6FA)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = assetImageBitmap("user.png"),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Juan Pérez", fontWeight = FontWeight.Bold)
                            Text(text = "Estudiante Activo", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ID DEL ESTUDIANTE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(text = "2023001", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "CARRERA", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(text = "Ingeniería", fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Confirmar Asistencia", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FailureView(onRetry: () -> Unit, onManual: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("!", fontSize = 32.sp, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Registro fallido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "El código QR es inválido o ha expirado. Por favor, inténtalo de nuevo o busca manualmente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Intentar de nuevo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedButton(
                    onClick = onManual,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Buscar manualmente", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ManualRegistrationView(
    manualId: String, 
    onIdChange: (String) -> Unit, 
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var searchPerformed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro Manual",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Matrícula del Alumno", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = manualId,
                onValueChange = onIdChange,
                placeholder = { Text("Ingresar matrícula del alumno") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { searchPerformed = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                    bitmap = assetImageBitmap("home.png"), // Lupa placeholder
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buscar Alumno", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        if (searchPerformed) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "RESULTADOS DE BÚSQUEDA", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                bitmap = assetImageBitmap("user.png"),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = "Carlos Ramirez", fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        bitmap = assetImageBitmap("user.png"), // Icono carrera placeholder
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Ingeniería en Sistemas", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "ESTADO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(text = "Verificado", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "ID MATRÍCULA", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(text = "202400129", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        OutlinedButton(
                            onClick = onRegisterSuccess,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2F6FED))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    bitmap = assetImageBitmap("user.png"), // Icono registro placeholder
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registrar Asistencia")
                            }
                        }
                    }
                }
            }
        }
        
        TextButton(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Regresar al escáner", color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScannerScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminScannerScreen()
    }
}