package com.example.parkinglot.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @GET("parkinglot")
    suspend fun getAllParkinglot(): Response<ResponseListParkinglot>

    @POST("parkinglot")
    suspend fun saveParkinglot(@Body d: NewRequestDataParkinglot): Response<ResponseParkinglot>

    @PUT("parkinglot/{id}")
    suspend fun updateParkinglot(@Path("id") id: String, @Body d: DataParkinglot): Response<ResponseParkinglot>

    @DELETE("parkinglot/{id}")
    suspend fun deleteParkinglot(@Path("id") id: String): Response<ResponseParkinglot>
}