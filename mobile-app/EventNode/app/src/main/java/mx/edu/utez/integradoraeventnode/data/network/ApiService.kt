package mx.edu.utez.integradoraeventnode.data.network

import mx.edu.utez.integradoraeventnode.data.network.models.AlumnoRegistroRequest
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.data.network.models.LoginRequest
import mx.edu.utez.integradoraeventnode.data.network.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/alumnos/registro")
    suspend fun registrarAlumno(@Body request: AlumnoRegistroRequest): Response<Map<String, String>>

    @GET("/api/eventos")
    suspend fun getEventos(): Response<List<EventoResponse>>

    @GET("/api/eventos/{id}")
    suspend fun getEvento(@retrofit2.http.Path("id") id: Int): Response<EventoResponse>

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
        @retrofit2.http.Path("idUsuario") idUsuario: Int
    ): Response<List<Map<String, Any>>>
}
