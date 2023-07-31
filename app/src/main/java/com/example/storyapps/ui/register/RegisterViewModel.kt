package com.example.storyapps.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyapps.data.AuthRepository
import com.example.storyapps.service.User

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun register(user: User) = authRepository.register(user)
}