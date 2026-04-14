package mx.edu.utez.integradoraeventnode.data.network.models

data class AlumnoRegistroRequest(
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val matricula: String,
    val correo: String,
    val password: String,
    val fechaNacimiento: String, // yyyy-MM-dd
    val sexo: String,
    val cuatrimestre: Int
)
