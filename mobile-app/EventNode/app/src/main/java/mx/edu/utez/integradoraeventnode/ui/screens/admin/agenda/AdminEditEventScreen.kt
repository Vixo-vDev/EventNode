package mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme
import mx.edu.utez.integradoraeventnode.ui.utils.assetImageBitmap

@Composable
fun AdminEditEventScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var title by remember { mutableStateOf("Festival en el Campus") }
    var date by remember { mutableStateOf("Viernes, 24 Octubre, 2024") }
    var time by remember { mutableStateOf("16:00 - 20:00") }
    var location by remember { mutableStateOf("Auditorio Principal") }
    var capacity by remember { mutableStateOf("300") }
    var description by remember { mutableStateOf("Únete a nosotros en la celebración anual del campus...") }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF5F6FA)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        EditEventField(label = "TÍTULO DEL EVENTO", value = title, onValueChange = { title = it }, icon = "book-open-reader.png")
                        EditEventField(label = "FECHA", value = date, onValueChange = { date = it }, icon = "book-open-reader.png")
                        EditEventField(label = "HORARIO", value = time, onValueChange = { time = it }, icon = "book-open-reader.png")
                        EditEventField(label = "UBICACIÓN", value = location, onValueChange = { location = it }, icon = "home.png")
                        EditEventField(label = "CAPACIDAD MÁXIMA", value = capacity, onValueChange = { capacity = it }, icon = "user.png")
                        
                        Text(text = "DESCRIPCIÓN", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF5F6FA),
                                unfocusedContainerColor = Color(0xFFF5F6FA),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED))
                ) {
                    Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun EditEventField(label: String, value: String, onValueChange: (String) -> Unit, icon: String? = null) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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

@Preview(showBackground = true)
@Composable
fun AdminEditEventScreenPreview() {
    IntegradoraEventNodeTheme {
        AdminEditEventScreen()
    }
}