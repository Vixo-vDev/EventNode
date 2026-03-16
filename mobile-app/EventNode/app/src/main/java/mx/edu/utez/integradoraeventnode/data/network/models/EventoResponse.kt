package mx.edu.utez.integradoraeventnode.data.network.models

data class EventoResponse(
    val idEvento: Int,
    val banner: String?,
    val nombre: String,
    val ubicacion: String,
    val capacidadMaxima: Int,
    val tiempoCancelacionHoras: Int?,
    val fechaInicio: String, // e.g. "2024-03-25T10:00:00"
    val fechaFin: String, // e.g. "2024-03-25T12:00:00"
    val tiempoToleranciaMinutos: Int?,
    val descripcion: String,
    val estado: String,
    val idCategoria: Int?,
    val nombreCategoria: String?
)
