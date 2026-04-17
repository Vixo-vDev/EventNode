package mx.edu.utez.integradoraeventnode.data.network.models

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: DTO de autenticacion recibido por Mobile tras el login.
 * Por que existe: define el contrato de sesion y datos de usuario que persiste la app tras autenticarse.
 *
 * Origen de datos: POST /api/auth/login con body { correo, password }.
 */
data class LoginResponse(
    val mensaje: String,
    val rol: String?,           // "ALUMNO" | "ADMINISTRADOR" | "SUPERADMIN"
    val idUsuario: Int?,
    val nombre: String?,
    val apellidoPaterno: String?,
    val apellidoMaterno: String?,
    val correo: String?,
    val matricula: String?,     // Solo presente para rol ALUMNO
    val sexo: String?,
    val cuatrimestre: Int?,     // Solo presente para rol ALUMNO
    val token: String?          // JWT Bearer token, prefijarlo con "Bearer "
)
