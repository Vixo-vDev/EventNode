package mx.edu.utez.integradoraeventnode.data.network.models

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: modelo de transporte que representa el DTO de eventos retornado por backend.
 * Por que existe: mantiene contrato estable Mobile <-> Backend para parsear respuestas sin ambiguedad.
 *
 * Origen de datos: GET /api/eventos y GET /api/eventos/{id}.
 */
data class EventoResponse(
    val idEvento: Int,
    val banner: String?,
    val nombre: String,
    val ubicacion: String,
    val capacidadMaxima: Int,
    val tiempoCancelacionHoras: Int?,
    val fechaInicio: String,  // Formato ISO: "2024-03-25T10:00:00"
    val fechaFin: String,     // Formato ISO: "2024-03-25T12:00:00"
    val tiempoToleranciaMinutos: Int?,
    val descripcion: String,
    val estado: String,       // Valores: "PUBLICADO", "CANCELADO", "FINALIZADO"
    val categoriaId: Int?,
    val categoriaNombre: String?,  // ← nombre exacto que devuelve el backend
    val inscritos: Long?           // conteo de pre-checkins ACTIVOS
)
