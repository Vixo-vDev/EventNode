package mx.edu.utez.integradoraeventnode.data.network

import mx.edu.utez.integradoraeventnode.data.network.models.AlumnoRegistroRequest
import mx.edu.utez.integradoraeventnode.data.network.models.EventoResponse
import mx.edu.utez.integradoraeventnode.data.network.models.LoginRequest
import mx.edu.utez.integradoraeventnode.data.network.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/alumnos/registro")
    suspend fun registrarAlumno(@Body request: AlumnoRegistroRequest): Response<Map<String, String>>

    @GET("/api/eventos")
    suspend fun getEventos(): Response<List<EventoResponse>>
}
