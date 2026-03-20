package mx.edu.utez.integradoraeventnode.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Acceso centralizado a SharedPreferences.
 * Elimina la repetición de getSharedPreferences("EventNodePrefs", ...) en 31 archivos.
 */
object PreferencesHelper {

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(context: Context): String =
        prefs(context).getString(Constants.KEY_TOKEN, "") ?: ""

    fun getBearerToken(context: Context): String {
        val token = getToken(context)
        return if (token.isNotEmpty()) "${Constants.BEARER_PREFIX}$token" else ""
    }

    fun getUserId(context: Context): Int =
        prefs(context).getInt(Constants.KEY_ID, -1)

    fun getNombre(context: Context): String =
        prefs(context).getString(Constants.KEY_NOMBRE, "") ?: ""

    fun getApellidoPaterno(context: Context): String =
        prefs(context).getString(Constants.KEY_APELLIDO_PATERNO, "") ?: ""

    fun getApellidoMaterno(context: Context): String =
        prefs(context).getString(Constants.KEY_APELLIDO_MATERNO, "") ?: ""

    fun getCorreo(context: Context): String =
        prefs(context).getString(Constants.KEY_CORREO, "") ?: ""

    fun getMatricula(context: Context): String =
        prefs(context).getString(Constants.KEY_MATRICULA, "") ?: ""

    fun getSexo(context: Context): String =
        prefs(context).getString(Constants.KEY_SEXO, "") ?: ""

    fun getCuatrimestre(context: Context): String =
        prefs(context).getString(Constants.KEY_CUATRIMESTRE, "") ?: ""

    fun getRol(context: Context): String =
        prefs(context).getString(Constants.KEY_ROL, "") ?: ""

    fun getMantenerSesion(context: Context): Boolean =
        prefs(context).getBoolean(Constants.KEY_MANTENER_SESION, false)

    fun setMantenerSesion(context: Context, value: Boolean) {
        prefs(context).edit().putBoolean(Constants.KEY_MANTENER_SESION, value).apply()
    }

    fun getFullName(context: Context): String {
        val nombre = getNombre(context)
        val paterno = getApellidoPaterno(context)
        val materno = getApellidoMaterno(context)
        return "$nombre $paterno $materno".trim()
    }
}
