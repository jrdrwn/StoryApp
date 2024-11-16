package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.picodiploma.loginwithanimation.AppExecutors
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.db.Story
import com.dicoding.picodiploma.loginwithanimation.data.db.StoryDao
import com.dicoding.picodiploma.loginwithanimation.data.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
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
    private val appExecutors: AppExecutors,
    private val storyDao: StoryDao
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
                    val stories = response.body()?.listStory
                    val storiesList = ArrayList<Story>()
                    appExecutors.diskIO.execute {
                        stories?.forEach {
                            val story = Story(
                                storyId = it.id,
                                name = it.name,
                                description = it.description,
                                photoUrl = it.photoUrl
                            )
                            storiesList.add(story)
                        }
                        storyDao.deleteAllStories()
                        storyDao.insertAll(storiesList)
                    }
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
        val localData = storyDao.getAllStories()
        getStoriesResult.addSource(localData) {
            getStoriesResult.value = Result.Success(StoryResponse(listStory = it.map { story ->
                ListStoryItem(
                    id = story.storyId,
                    name = story.name,
                    description = story.description,
                    photoUrl = story.photoUrl
                )
            }))
        }
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
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
            storyDao: StoryDao
        ): StoryRepository = StoryRepository(apiService, appExecutors, storyDao)
    }
}