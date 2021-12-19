package com.example.petadoptionapp.httpservice

import com.example.petadoptionapp.*
import com.example.petadoptionapp.data.IpResult
import com.example.petadoptionapp.data.PetIdClass
import com.example.petadoptionapp.data.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HttpApiService {

    @GET("pets")
    suspend fun getAllPets(@Header("Authorization") token: String): Response<PetListModel>

    @POST("login")
    suspend fun login(@Body user: User): IpResult

    @POST("register")
    suspend fun register(@Body user: User): ResponseBody

    @POST("users/me/petInterests")
    suspend fun createNewPetInterest(@Body petId: PetIdClass)

    @POST("users/me/email")
    suspend fun changeEmail(@Body email: ChangedEmail) : ResponseBody

    @GET("users/me/petInterests")
    suspend fun getPetInterest(@Header("Authorization") token: String) :Response<InterestedListModel>

    @DELETE("users/me/petInterests/{petInterestId}")
    suspend fun deletePetInterest(@Path("petInterestId") petInterestId:Int)

    @DELETE("users/me")
    suspend fun deleteAcc() : ResponseBody

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String) : Response<UsersData>

    @GET("users/me/loginHistory")
    suspend fun getHistory(@Header("Authorization") token: String): Response<LoginHistoryDTO>

}