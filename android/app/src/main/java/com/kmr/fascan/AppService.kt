package com.kmr.fascan

import com.kmr.fascan.models.Face
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppService {

    @Multipart
    @POST("detect")
    suspend fun detectFace(@Part payload: MultipartBody.Part): Face

    companion object {
        private const val BASE_URL = BuildConfig.API_BASE_URL
        private lateinit var instance: AppService

        fun create(): AppService {
            if (!this::instance.isInitialized) {
                val logger =
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AppService::class.java)
            }

            return instance
        }
    }
}