package com.test.postsmobiletest.repository


import com.test.postsmobiletest.network.RetrofitService

class MainRepository(private val retrofitService: RetrofitService, private val postId: Int?) {
    fun getAllPosts() = retrofitService.getAllPost()
    fun getAllComments() = retrofitService.getAllComments(postId!!)
}