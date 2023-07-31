package com.example.storyapps.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.helper.UserPreferences
import com.example.storyapps.ui.story.StoryActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val pref = UserPreferences.getInstance(dataStore)
        val factory = ViewModelFactory(pref)
        viewModel = ViewModelProvider(
            this, factory
        )[MainViewModel::class.java]

        setupViewModelObserver()
    }

    private fun setupViewModelObserver() {
        val intentToAuth = Intent(this, AuthenActivity::class.java)
        val intentToStory = Intent(this, StoryActivity::class.java)

        viewModel.getUserToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                startActivity(intentToStory)
                finish()
            } else {
                startActivity(intentToAuth)
                finish()
            }
        }
    }
}