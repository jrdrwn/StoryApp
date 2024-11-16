package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() = storyRepository.getStories()
}