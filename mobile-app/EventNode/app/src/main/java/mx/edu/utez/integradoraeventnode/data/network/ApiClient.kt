package mx.edu.utez.integradoraeventnode.data.network

import mx.edu.utez.integradoraeventnode.data.network.ApiService
import mx.edu.utez.integradoraeventnode.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: punto unico de conexion Mobile -> Backend mediante Retrofit.
 * Por que existe: centraliza URL, cliente HTTP y serializacion para evitar configuraciones inconsistentes.
 *
 * IP del backend: se define en local.properties como BACKEND_IP y se expone via BuildConfig.
 * Token JWT: se envia por endpoint en ApiService.kt con @Header(\"Authorization\") usando PreferencesHelper.
 */
object ApiClient {
    private const val BASE_URL = "http://${BuildConfig.BACKEND_IP}:8080"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /** Instancia única de Retrofit lista para llamadas a todos los endpoints */
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
