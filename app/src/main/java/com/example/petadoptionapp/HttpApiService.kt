package com.example.petadoptionapp

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.*

interface HttpApiService {


    @GET("pets")
    suspend fun getAllPets(@Header("Authorization") token: String): List<PetsModel>

    @POST("login")
    suspend fun login(@Body user: User):IpResult

    @POST("register")
    suspend fun register(@Body user: User):ResponseBody
}