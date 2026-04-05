package mx.edu.utez.integradoraeventnode.ui.screens.admin.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav
import mx.edu.utez.integradoraeventnode.utils.PreferencesHelper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner

enum class ScannerState {
    EVENT_SELECT,
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
    var scannerState by remember { mutableStateOf(ScannerState.EVENT_SELECT) }
    var manualId by remember { mutableStateOf("") }
    var selectedEvent by remember { mutableStateOf<EventoResponse?>(null) }
    var successStudentName by remember { mutableStateOf("") }
    var failureMessage by remember { mutableStateOf("") }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                when (scannerState) {
                    ScannerState.EVENT_SELECT -> EventSelectionView(
                        onEventSelected = { event ->
                            selectedEvent = event
                            scannerState = ScannerState.INITIAL
                        }
                    )
                    ScannerState.INITIAL -> {
                        if (selectedEvent != null) {
                            InitialScannerView(
                                event = selectedEvent!!,
                                onScan = { scannerState = ScannerState.SCANNING },
                                onManual = { scannerState = ScannerState.MANUAL },
                                onChangeEvent = { scannerState = ScannerState.EVENT_SELECT }
                            )
                        }
                    }
                    ScannerState.SCANNING -> {
                        if (selectedEvent != null) {
                            ScanningView(
                                event = selectedEvent!!,
                                onBack = { scannerState = ScannerState.INITIAL },
                                onScanSuccess = { studentInfo ->
                                    successStudentName = studentInfo
                                    scannerState = ScannerState.SUCCESS
                                },
                                onScanFailure = { error ->
                                    failureMessage = error
                                    scannerState = ScannerState.FAILURE
                                }
                            )
                        }
                    }
                    ScannerState.SUCCESS -> SuccessView(
                        studentName = successStudentName,
                        onConfirm = {
                            manualId = ""
                            successStudentName = ""
                            scannerState = ScannerState.INITIAL
                        }
                    )
                    ScannerState.FAILURE -> FailureView(
                        errorMessage = failureMessage,
                        onRetry = { scannerState = ScannerState.SCANNING },
                        onManual = { scannerState = ScannerState.MANUAL }
                    )
                    ScannerState.MANUAL -> {
                        if (selectedEvent != null) {
                            ManualRegistrationView(
                                event = selectedEvent!!,
                                manualId = manualId,
                                onIdChange = { manualId = it },
                                onBack = { scannerState = ScannerState.INITIAL },
                                onRegisterSuccess = { studentName ->
                                    successStudentName = studentName
                                    manualId = ""
                                    scannerState = ScannerState.SUCCESS
                                },
                                onError = { error ->
                                    failureMessage = error
                                    scannerState = ScannerState.FAILURE
                                }
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
private fun EventSelectionView(onEventSelected: (EventoResponse) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var events by remember { mutableStateOf<List<EventoResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""

                if (token.isEmpty()) {
                    error = "Token no disponible"
                    isLoading = false
                    return@launch
                }

                val response = ApiClient.apiService.getEventosFiltrados("Bearer $token", estado = "ACTIVO")
                if (response.isSuccessful && response.body() != null) {
                    events = response.body() ?: emptyList()
                } else {
                    error = "Error al cargar eventos: ${response.code()}"
                }
                isLoading = false
            } catch (e: Exception) {
                error = "Error: ${e.message}"
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Seleccionar Evento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color(0xFF2F6FED))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Cargando eventos activos...", color = Color.Gray)
                    }
                }
            }
            error.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error, color = Color(0xFFD32F2F), textAlign = TextAlign.Center)
                }
            }
            events.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay eventos activos disponibles", color = Color.Gray, textAlign = TextAlign.Center)
                }
            }
            else -> {
                events.forEach { event ->
                    ScannerEventCard(event = event, onSelect = { onEventSelected(event) })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ScannerEventCard(event: EventoResponse, onSelect: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    val timeRange = try {
        val inicio = LocalDateTime.parse(event.fechaInicio.take(19), formatter)
        val fin = LocalDateTime.parse(event.fechaFin.take(19), formatter)
        "${inicio.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${fin.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    } catch (e: Exception) { event.fechaInicio.take(16).replace("T", " ") }

    val bannerBitmap: ImageBitmap? = remember(event.banner) {
        if (event.banner.isNullOrEmpty()) return@remember null
        try {
            val clean = if (event.banner!!.contains(",")) event.banner!!.substringAfter(",") else event.banner!!
            val bytes = Base64.decode(clean, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) { null }
    }

    val accentColor = Color(0xFF2F6FED)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(accentColor.copy(alpha = 0.85f))
            ) {
                if (bannerBitmap != null) {
                    val isLive = false
                    Image(
                        bitmap = bannerBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = if (isLive) 0.5f else 0.7f
                    )
                }
                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ACTIVO",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = event.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = timeRange, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = event.ubicacion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onSelect,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Image(
                        bitmap = assetImageBitmap("qr-scan.png"),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Seleccionar para escanear", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InitialScannerView(
    event: EventoResponse,
    onScan: () -> Unit,
    onManual: () -> Unit,
    onChangeEvent: () -> Unit
) {
    val context = LocalContext.current
    val adminName = PreferencesHelper.getFullName(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar — igual al alumno
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { onChangeEvent() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("arrow-small-left.png"),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "Escanear QR",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card principal — misma estructura que CheckinQrScreen
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = event.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1A1C1E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (adminName.isNotEmpty()) {
                    Text(
                        text = adminName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Administrador",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF2F6FED),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Icono de escaneo donde el alumno tiene el QR
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF5F6FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            bitmap = assetImageBitmap("qr-scan.png"),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Listo para\nescanear",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Info box — igual al alumno
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6F0FF))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Apunta la cámara al código QR del alumno para registrar su asistencia.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2F6FED),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón principal
        Button(
            onClick = onScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
        ) {
            Image(
                bitmap = assetImageBitmap("qr-scan.png"),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Escanear", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Alternativa: registro manual
        OutlinedButton(
            onClick = onManual,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2F6FED))
        ) {
            Text("Registro Manual", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onChangeEvent, modifier = Modifier.padding(top = 2.dp)) {
            Text("Cambiar evento", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ScanningView(
    event: EventoResponse,
    onBack: () -> Unit,
    onScanSuccess: (String) -> Unit,
    onScanFailure: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }
    var hasScanned by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun processQrCode(qrData: String) {
        if (isProcessing || hasScanned) return
        hasScanned = true
        isProcessing = true

        coroutineScope.launch {
            try {
                val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", "") ?: ""

                if (token.isEmpty()) {
                    onScanFailure("Token no disponible")
                    return@launch
                }

                val response = ApiClient.apiService.registrarAsistenciaManual(
                    "Bearer $token",
                    mapOf(
                        "matricula" to qrData.trim(),
                        "idEvento" to event.idEvento
                    )
                )

                if (response.isSuccessful) {
                    onScanSuccess(qrData.trim())
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val errorMsg = if (errorBody.contains("mensaje")) {
                        errorBody.substringAfter("\"mensaje\":\"").substringBefore("\"")
                    } else {
                        "Error al registrar asistencia"
                    }
                    onScanFailure(errorMsg)
                }
            } catch (e: Exception) {
                onScanFailure("Error: ${e.message}")
            } finally {
                isProcessing = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("arrow-small-left.png"),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "Escanear QR del alumno",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event.nombre,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF2F6FED),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!hasPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Se requiere permiso de cámara para escanear.", textAlign = TextAlign.Center, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("Conceder permiso")
                    }
                }
            }
        } else {
            val lifecycleOwner = LocalLifecycleOwner.current

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { ctx ->
                            DecoratedBarcodeView(ctx).apply {
                                barcodeView.decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
                                setStatusText("")
                                decodeContinuous(object : BarcodeCallback {
                                    override fun barcodeResult(result: BarcodeResult?) {
                                        if (result != null) processQrCode(result.text)
                                    }
                                    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
                                })
                                val observer = LifecycleEventObserver { _, event ->
                                    when (event) {
                                        Lifecycle.Event.ON_RESUME -> resume()
                                        Lifecycle.Event.ON_PAUSE -> pause()
                                        else -> {}
                                    }
                                }
                                lifecycleOwner.lifecycle.addObserver(observer)
                            }
                        },
                        update = { view -> view.resume() },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isProcessing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE6F0FF))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "ESCÁNER ACTIVO",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2F6FED),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun SuccessView(studentName: String, onConfirm: () -> Unit) {
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
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFF4CAF50))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Asistencia Registrada!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = studentName, fontWeight = FontWeight.Bold)
                            Text(text = "Asistencia registrada", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
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
                    Text("Continuar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FailureView(errorMessage: String, onRetry: () -> Unit, onManual: () -> Unit) {
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
                    text = errorMessage.ifEmpty { "El código QR es inválido o ha expirado. Por favor, inténtalo de nuevo o busca manualmente." },
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
    event: EventoResponse,
    manualId: String,
    onIdChange: (String) -> Unit,
    onBack: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var registrationDone by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar — igual estilo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = assetImageBitmap("arrow-small-left.png"),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "Registro Manual",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Evento info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F7FF))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2F6FED).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = assetImageBitmap("book-open-reader.png"),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF2F6FED))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "EVENTO",
                            fontSize = 9.sp,
                            color = Color(0xFF2F6FED),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = event.nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Matrícula del alumno",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF44474E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = manualId,
                    onValueChange = {
                        onIdChange(it)
                        registrationDone = false
                    },
                    placeholder = { Text("Ej. 220001234") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Resultado exitoso inline
                if (registrationDone) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color.White)
                    )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Asistencia registrada",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "Matrícula: $manualId",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = {
                        if (manualId.isNotEmpty() && !isLoading) {
                            isLoading = true
                            registrationDone = false
                            coroutineScope.launch {
                                try {
                                    val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                                    val token = prefs.getString("token", "") ?: ""
                                    if (token.isEmpty()) {
                                        onError("Token no disponible")
                                        isLoading = false
                                        return@launch
                                    }
                                    val response = ApiClient.apiService.registrarAsistenciaManual(
                                        "Bearer $token",
                                        mapOf(
                                            "matricula" to manualId,
                                            "idEvento" to event.idEvento
                                        )
                                    )
                                    if (response.isSuccessful) {
                                        registrationDone = true
                                    } else {
                                        val errorBody = response.errorBody()?.string() ?: ""
                                        val errorMsg = if (errorBody.contains("mensaje")) {
                                            errorBody.substringAfter("\"mensaje\":\"").substringBefore("\"")
                                        } else {
                                            "Error al registrar asistencia (${response.code()})"
                                        }
                                        onError(errorMsg)
                                    }
                                } catch (e: Exception) {
                                    onError("Error: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
                    enabled = manualId.isNotEmpty() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registrando...")
                    } else {
                        Image(
                            bitmap = assetImageBitmap("user.png"),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registrar Asistencia", fontWeight = FontWeight.Bold)
                    }
                }

                if (registrationDone) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { onRegisterSuccess(manualId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50))
                    ) {
                        Text("Continuar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("Volver al escáner QR", color = Color.Gray, fontSize = 13.sp)
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