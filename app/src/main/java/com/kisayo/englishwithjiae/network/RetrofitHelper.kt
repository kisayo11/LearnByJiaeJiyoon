package com.kisayo.englishwithjiae.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {
    companion object {
        fun getRetrofitInstance(): Retrofit {
            val retrofit = Retrofit.Builder().run {
                baseUrl("http://mrhi2424.dothome.co.kr")
                addConverterFactory(ScalarsConverterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
                build()
            }
            return retrofit
        }
    }
}