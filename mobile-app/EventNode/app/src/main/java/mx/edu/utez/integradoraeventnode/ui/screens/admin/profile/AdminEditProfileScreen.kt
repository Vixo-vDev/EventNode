package mx.edu.utez.integradoraeventnode.ui.screens.admin.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import android.content.Context
import mx.edu.utez.integradoraeventnode.R
import kotlinx.coroutines.launch
import mx.edu.utez.integradoraeventnode.data.network.ApiClient
import mx.edu.utez.integradoraeventnode.utils.PreferencesHelper
import mx.edu.utez.integradoraeventnode.utils.AppColors
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap
import mx.edu.utez.integradoraeventnode.ui.screens.admin.common.AdminBottomNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProfileScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onAgenda: () -> Unit = {},
    onEscanear: () -> Unit = {},
    onDiplomas: () -> Unit = {},
    onAnalitica: () -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(PreferencesHelper.getNombre(context)) }
    var apellidoPaterno by remember { mutableStateOf(PreferencesHelper.getApellidoPaterno(context)) }
    var apellidoMaterno by remember { mutableStateOf(PreferencesHelper.getApellidoMaterno(context)) }
    val correo = PreferencesHelper.getCorreo(context)

    var saving by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image with Edit Badge
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            bitmap = assetImageBitmap("user.png"),
                            contentDescription = stringResource(R.string.edit_profile_photo),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AppColors.Primary)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = assetImageBitmap("vista.png"),
                                contentDescription = stringResource(R.string.edit_profile_edit_photo),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Form Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            EditField(
                                label = stringResource(R.string.edit_profile_name),
                                value = nombre,
                                onValueChange = { nombre = it },
                                icon = "user.png"
                            )
                            EditField(
                                label = stringResource(R.string.edit_profile_last_name_p),
                                value = apellidoPaterno,
                                onValueChange = { apellidoPaterno = it },
                                icon = "user.png"
                            )
                            EditField(
                                label = stringResource(R.string.edit_profile_last_name_m),
                                value = apellidoMaterno,
                                onValueChange = { apellidoMaterno = it },
                                icon = "user.png"
                            )
                            // Correo - read only
                            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                                Text(text = stringResource(R.string.edit_profile_email), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = correo,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFE8E8E8),
                                        unfocusedContainerColor = Color(0xFFE8E8E8),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    leadingIcon = {
                                        Image(
                                            bitmap = assetImageBitmap("correo.png"),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (nombre.isBlank() || apellidoPaterno.isBlank()) {
                                errorMessage = context.getString(R.string.edit_profile_required)
                                showErrorDialog = true
                                return@Button
                            }
                            saving = true
                            scope.launch {
                                try {
                                    val token = PreferencesHelper.getBearerToken(context)
                                    val userId = PreferencesHelper.getUserId(context)
                                    val body = mapOf<String, Any>(
                                        "nombre" to nombre.trim(),
                                        "apellidoPaterno" to apellidoPaterno.trim(),
                                        "apellidoMaterno" to apellidoMaterno.trim()
                                    )
                                    val response = ApiClient.apiService.actualizarPerfil(token, userId, body)
                                    if (response.isSuccessful) {
                                        // Update SharedPreferences
                                        val prefs = context.getSharedPreferences("EventNodePrefs", Context.MODE_PRIVATE)
                                        prefs.edit()
                                            .putString("nombre", nombre.trim())
                                            .putString("apellidoPaterno", apellidoPaterno.trim())
                                            .putString("apellidoMaterno", apellidoMaterno.trim())
                                            .apply()
                                        showSuccessDialog = true
                                    } else {
                                        val errorBody = response.errorBody()?.string() ?: ""
                                        errorMessage = if (errorBody.contains("mensaje")) {
                                            try {
                                                org.json.JSONObject(errorBody).getString("mensaje")
                                            } catch (e: Exception) {
                                                context.getString(R.string.edit_profile_error)
                                            }
                                        } else {
                                            context.getString(R.string.edit_profile_error)
                                        }
                                        showErrorDialog = true
                                    }
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: "Error de conexión"
                                    showErrorDialog = true
                                } finally {
                                    saving = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                        enabled = !saving
                    ) {
                        if (saving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(stringResource(R.string.edit_profile_save), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                    selected = "Perfil",
                    onHome = onHome,
                    onAgenda = onAgenda,
                    onEscanear = onEscanear,
                    onDiplomas = onDiplomas,
                    onAnalitica = onAnalitica,
                    onProfile = onProfile
                )
            }
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            onBackToProfile = {
                showSuccessDialog = false
                onBack()
            },
            onGoHome = {
                showSuccessDialog = false
                onHome()
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(R.string.common_error), fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(stringResource(R.string.common_accept), color = AppColors.Primary)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F6FA),
                unfocusedContainerColor = Color(0xFFF5F6FA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = if (icon != null) {
                {
                    Image(
                        bitmap = assetImageBitmap(icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null
        )
    }
}

@Composable
private fun SuccessDialog(onBackToProfile: () -> Unit, onGoHome: () -> Unit) {
    Dialog(onDismissRequest = onBackToProfile) {
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
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.White))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¡Datos actualizados!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu información ha sido guardada correctamente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onBackToProfile,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    Text(stringResource(R.string.edit_profile_back), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onGoHome,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F6FA))
                ) {
                    Text(stringResource(R.string.edit_profile_go_home), color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
