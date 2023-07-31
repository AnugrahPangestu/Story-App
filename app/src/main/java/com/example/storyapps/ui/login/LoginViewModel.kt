package com.example.storyapps.ui.login

import androidx.lifecycle.*
import com.example.storyapps.data.AuthRepository
import com.example.storyapps.service.User

class LoginViewModel (private val authRepository: AuthRepository) : ViewModel() {

    fun getUserToken() = authRepository.getUserToken()

    fun login(user: User) = authRepository.login(user)
}