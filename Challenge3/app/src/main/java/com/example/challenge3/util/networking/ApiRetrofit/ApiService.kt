package com.example.challenge3.util.networking.ApiRetrofit

import com.example.challenge3.util.networking.Response.CategoryResponse
import com.example.challenge3.util.networking.Response.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    //untuk mapping Endpoint API
    @GET("category")
    fun getCategory():Call<CategoryResponse>

    @GET("category")
    suspend fun getCategory2():CategoryResponse

    @GET("listmenu")
    suspend fun getAllFoods():FoodResponse


    @POST("order")
    fun postOrder()

}