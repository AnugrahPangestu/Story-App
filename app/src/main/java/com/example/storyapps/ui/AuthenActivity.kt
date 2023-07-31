package com.example.storyapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapps.R

class AuthenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authen)

        supportActionBar?.hide()
    }
}