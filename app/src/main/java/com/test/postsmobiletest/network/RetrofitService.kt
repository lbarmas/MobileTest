package com.test.postsmobiletest.network

import com.test.postsmobiletest.model.CommentPost
import com.test.postsmobiletest.model.Post
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("comments")
    fun getAllComments(@Query("postId") postId: Int) : Call<List<CommentPost>>

    @GET("/posts")
    fun getAllPost() : Call<List<Post>>

    companion object {

        var retrofitService: RetrofitService? = null
        var BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}