package mx.edu.utez.integradoraeventnode.data.network

import mx.edu.utez.integradoraeventnode.data.network.ApiService
import mx.edu.utez.integradoraeventnode.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // La IP se lee desde local.properties (BACKEND_IP=tu.ip.aqui)
    // Cada miembro del equipo configura su propia IP sin afectar a los demás
    private const val BASE_URL = "http://${BuildConfig.BACKEND_IP}:8080"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
