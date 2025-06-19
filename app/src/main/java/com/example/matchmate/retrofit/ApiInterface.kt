package com.example.matchmate.retrofit

import com.example.matchmate.modelclass.MatchMate
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("api/")
    suspend fun getUsers(
        @Query("results") results: Int = 10,
    ): Response<MatchMate>

}