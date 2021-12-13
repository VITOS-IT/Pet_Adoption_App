package com.example.petadoptionapp

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class MyPetAppApi: Application() {

    public lateinit var httpApiService: HttpApiService
    override fun onCreate() {
        super.onCreate()

        httpApiService = initPetApiService()
    }

    private fun initPetApiService(): HttpApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kanini-course.cloud/doggeforlife-mobile/")
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
            .build()
        return retrofit.create(HttpApiService::class.java)
    }
}