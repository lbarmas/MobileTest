package com.test.postsmobiletest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.postsmobiletest.model.CommentPost
import com.test.postsmobiletest.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel constructor(private val repository: MainRepository): ViewModel() {

    val commentsList = MutableLiveData<List<CommentPost>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllComment(postId: Int) {
        val response = repository.getAllComments()
        response.enqueue(object : Callback<List<CommentPost>> {
            override fun onResponse(call: Call<List<CommentPost>>, response: Response<List<CommentPost>>) {
                commentsList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<CommentPost>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}