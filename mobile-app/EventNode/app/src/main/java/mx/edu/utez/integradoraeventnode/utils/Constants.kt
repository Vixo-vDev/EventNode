package mx.edu.utez.integradoraeventnode.utils

import androidx.compose.ui.graphics.Color

object Constants {
    // SharedPreferences
    const val PREFS_NAME = "EventNodePrefs"

    // Preference keys
    const val KEY_TOKEN = "token"
    const val KEY_ID = "id"
    const val KEY_NOMBRE = "nombre"
    const val KEY_APELLIDO_PATERNO = "apellidoPaterno"
    const val KEY_APELLIDO_MATERNO = "apellidoMaterno"
    const val KEY_CORREO = "correo"
    const val KEY_MATRICULA = "matricula"
    const val KEY_SEXO = "sexo"
    const val KEY_CUATRIMESTRE = "cuatrimestre"
    const val KEY_ROL = "rol"
    const val KEY_MANTENER_SESION = "mantenerSesion"

    // Auth
    const val BEARER_PREFIX = "Bearer "
}

object AppColors {
    val Primary = Color(0xFF2F6FED)
    val PrimaryDark = Color(0xFF1A56D6)
    val Background = Color(0xFFF5F6FA)
    val TextDark = Color(0xFF44474E)
    val TextGray = Color(0xFF999999)
    val CardBg = Color.White
    val Success = Color(0xFF4CAF50)
    val Error = Color(0xFFE53935)
    val Warning = Color(0xFFFFA726)
    val Accent = Color(0xFF7C4DFF)
}
