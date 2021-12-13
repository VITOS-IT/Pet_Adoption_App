package com.example.petadoptionapp

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface HttpApiService {


    @GET("pets")

    suspend fun getAllPets() : Response<PetListModel>

    @POST("login")
    suspend fun login(@Body user: User):IpResult

    @POST("register")
    suspend fun register(@Body user: User):ResponseBody
}