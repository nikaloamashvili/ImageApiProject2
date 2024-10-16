package com.example.fullproject.network

import com.example.fullproject.model.ExampleResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("api/")
    suspend fun getExampleData(@Query("key") key:String, @Query("q") q:String, @Query("image_type") image_type:String): Response<ExampleResponce>
}