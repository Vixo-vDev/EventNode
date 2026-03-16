package mx.edu.utez.integradoraeventnode.data.network.models

data class LoginResponse(
    val mensaje: String,
    val rol: String?,
    val idUsuario: Int?,
    val nombre: String?,
    val apellidoPaterno: String?,
    val apellidoMaterno: String?,
    val correo: String?,
    val matricula: String?,
    val sexo: String?,
    val cuatrimestre: Int?
)
