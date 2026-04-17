package mx.edu.utez.integradoraeventnode.data.network

import mx.edu.utez.integradoraeventnode.data.network.models.AlumnoRegistroRequest
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.data.network.models.LoginRequest
import mx.edu.utez.integradoraeventnode.data.network.models.LoginResponse
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Streaming

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: contrato Retrofit de endpoints que consume la app movil.
 * Por que existe: centraliza rutas y payloads para mantener sincronizado Mobile <-> Backend.
 */
interface ApiService {
    // ── Auth ──
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/recuperar/enviar-codigo")
    suspend fun enviarCodigoRecuperacion(@Body request: Map<String, String>): Response<Map<String, String>>

    @POST("/api/auth/recuperar/verificar-codigo")
    suspend fun verificarCodigo(@Body request: Map<String, String>): Response<Map<String, String>>

    @POST("/api/auth/recuperar/restablecer")
    suspend fun restablecerPassword(@Body request: Map<String, String>): Response<Map<String, String>>

    // ── Alumnos ──
    @POST("/api/alumnos/registro")
    suspend fun registrarAlumno(@Body request: AlumnoRegistroRequest): Response<Map<String, String>>

    @PUT("/api/alumnos/{id}")
    suspend fun actualizarAlumno(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    // ── Eventos ──
    @GET("/api/eventos")
    suspend fun getEventos(
        @Query("estado") estado: String? = null
    ): Response<List<EventoResponse>>

    @GET("/api/eventos")
    suspend fun getEventosFiltrados(
        @Header("Authorization") token: String,
        @Query("nombre") nombre: String? = null,
        @Query("mes") mes: Int? = null,
        @Query("categoriaId") categoriaId: Int? = null,
        @Query("estado") estado: String? = null
    ): Response<List<EventoResponse>>

    @GET("/api/eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): Response<EventoResponse>

    @GET("/api/eventos/categorias")
    suspend fun getCategorias(
        @Header("Authorization") token: String
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @POST("/api/eventos/crear")
    suspend fun crearEvento(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @PUT("/api/eventos/{id}")
    suspend fun actualizarEvento(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @DELETE("/api/eventos/{id}")
    suspend fun eliminarEvento(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, String>>

    @POST("/api/eventos/{id}/cancelar")
    suspend fun cancelarEvento(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, String>>

    @POST("/api/eventos/{id}/reactivar")
    suspend fun reactivarEvento(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, String>>

    // ── Organizadores ──
    @GET("/api/eventos/organizadores")
    suspend fun getOrganizadores(
        @Header("Authorization") token: String,
        @Query("nombre") nombre: String? = null
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @POST("/api/eventos/organizadores")
    suspend fun crearOrganizador(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @PUT("/api/eventos/organizadores/{id}")
    suspend fun actualizarOrganizador(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, String>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @DELETE("/api/eventos/organizadores/{id}")
    suspend fun eliminarOrganizador(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, String>>

    // ── PreCheckin ──
    @POST("/api/precheckin/inscribirse")
    suspend fun inscribirse(
        @Header("Authorization") token: String,
        @Body request: Map<String, Int>
    ): Response<Map<String, String>>

    @POST("/api/precheckin/cancelar")
    suspend fun cancelarInscripcion(
        @Header("Authorization") token: String,
        @Body request: Map<String, Int>
    ): Response<Map<String, String>>

    @GET("/api/precheckin/usuario/{idUsuario}")
    suspend fun listarMisEventos(
        @Header("Authorization") token: String,
        @Path("idUsuario") idUsuario: Int
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @GET("/api/precheckin/evento/{idEvento}")
    suspend fun listarInscritos(
        @Header("Authorization") token: String,
        @Path("idEvento") idEvento: Int
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @GET("/api/precheckin/evento/{idEvento}/count")
    suspend fun contarInscritos(
        @Header("Authorization") token: String,
        @Path("idEvento") idEvento: Int
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    // ── Asistencias ──
    @POST("/api/asistencias/registrar")
    suspend fun registrarAsistencia(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    @POST("/api/asistencias/manual")
    suspend fun registrarAsistenciaManual(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    @GET("/api/asistencias/evento/{idEvento}")
    suspend fun listarAsistencias(
        @Header("Authorization") token: String,
        @Path("idEvento") idEvento: Int
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @PATCH("/api/asistencias/{idAsistencia}/estado")
    suspend fun actualizarEstadoAsistencia(
        @Header("Authorization") token: String,
        @Path("idAsistencia") idAsistencia: Int,
        @Body request: Map<String, String>
    ): Response<Map<String, String>>

    @GET("/api/asistencias/evento/{idEvento}/count")
    suspend fun contarAsistencias(
        @Header("Authorization") token: String,
        @Path("idEvento") idEvento: Int
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    // ── Diplomas ──
    @POST("/api/diplomas/crear")
    suspend fun crearDiploma(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @GET("/api/diplomas/")
    suspend fun listarDiplomas(
        @Header("Authorization") token: String
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @GET("/api/diplomas/{idDiploma}")
    suspend fun obtenerDiploma(
        @Header("Authorization") token: String,
        @Path("idDiploma") idDiploma: Int
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @POST("/api/diplomas/{idDiploma}/emitir")
    suspend fun emitirDiplomas(
        @Header("Authorization") token: String,
        @Path("idDiploma") idDiploma: Int
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @GET("/api/diplomas/estudiante/{idUsuario}")
    suspend fun listarDiplomasEstudiante(
        @Header("Authorization") token: String,
        @Path("idUsuario") idUsuario: Int
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @PUT("/api/diplomas/{idDiploma}")
    suspend fun actualizarDiploma(
        @Header("Authorization") token: String,
        @Path("idDiploma") idDiploma: Int,
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @DELETE("/api/diplomas/{idDiploma}")
    suspend fun eliminarDiploma(
        @Header("Authorization") token: String,
        @Path("idDiploma") idDiploma: Int
    ): Response<Map<String, String>>

    @GET("/api/diplomas/{idDiploma}/descargar/{idUsuario}")
    @Streaming
    suspend fun descargarDiploma(
        @Header("Authorization") token: String,
        @Path("idDiploma") idDiploma: Int,
        @Path("idUsuario") idUsuario: Int
    ): Response<okhttp3.ResponseBody>

    // ── Usuarios ──
    @GET("/api/usuarios")
    suspend fun listarUsuarios(
        @Header("Authorization") token: String
    ): Response<List<Map<String, @JvmSuppressWildcards Any>>>

    @GET("/api/usuarios/{id}/perfil")
    suspend fun obtenerPerfil(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @POST("/api/usuarios/admin")
    suspend fun crearAdmin(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    @PATCH("/api/usuarios/{id}/estado")
    suspend fun cambiarEstado(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Map<String, String>>

    @PUT("/api/usuarios/{id}/perfil")
    suspend fun actualizarPerfil(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, @JvmSuppressWildcards Any>>
}
