package com.kisayo.englishwithjiae.network

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.PartMap

interface RetrofitService {

    @POST("/EnglishWithJiae/boardDB.php")
    fun postDatatoServer(@PartMap dataPart:Map<String,String>) : Call<String>

//    @GET("/EnglishWithJiae/loadDB.php")
//    fun loadDatatoServer(): Call<List<WordData>>
}