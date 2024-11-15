package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
) {
    private val getStoriesResult = MediatorLiveData<Result<StoryResponse>>()
    private val getDetailStoryResult = MediatorLiveData<Result<DetailStoryResponse>>()
    private val addStoryResult = MediatorLiveData<Result<AddStoryResponse>>()

    fun getStories(): LiveData<Result<StoryResponse>> {
        getStoriesResult.value = Result.Loading
        val client = apiService.getStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    getStoriesResult.value = Result.Success(response.body()!!)
                } else {
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    getStoriesResult.value = Result.Error(errorBody.message!!)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                getStoriesResult.value = Result.Error(t.message.toString())
            }
        })
        return getStoriesResult
    }

    fun getDetailStory(id: String): LiveData<Result<DetailStoryResponse>> {
        getDetailStoryResult.value = Result.Loading
        val client = apiService.getDetailStory(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    getDetailStoryResult.value = Result.Success(response.body()!!)
                } else {
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    getDetailStoryResult.value = Result.Error(errorBody.message!!)
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                getDetailStoryResult.value = Result.Error(t.message.toString())
            }
        })

        return getDetailStoryResult
    }

    fun addStory(
        description: String,
        imageFile: File
    ): LiveData<Result<AddStoryResponse>> {
        addStoryResult.value = Result.Loading
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = apiService.addStory(requestBody, multipartBody)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    addStoryResult.value = Result.Success(response.body()!!)
                } else {
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    addStoryResult.value = Result.Error(errorBody.message!!)
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                addStoryResult.value = Result.Error(t.message.toString())
            }
        })

        return addStoryResult
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}