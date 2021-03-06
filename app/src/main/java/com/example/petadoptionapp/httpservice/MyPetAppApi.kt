package com.example.petadoptionapp.httpservice

import android.app.Application
import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.Request

class MyPetAppApi: Application() {
    public lateinit var httpApiService: HttpApiService
    override fun onCreate() {
        super.onCreate()
        httpApiService = initPetApiService()
    }

    private fun initPetApiService(): HttpApiService {
        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        var token = mSettings.getString("token", "")!!
Log.d("somoe lodfjfigj: ",token)
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kanini-course.cloud/doggeforlife-mobile/")
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
            .client(client)
            .build()
        return retrofit.create(HttpApiService::class.java)
    }
}