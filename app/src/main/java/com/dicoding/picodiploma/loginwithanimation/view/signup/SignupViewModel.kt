package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)
}