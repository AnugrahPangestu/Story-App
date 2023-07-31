package com.example.storyapps.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.storyapps.R
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.helper.UserPreferences
import com.example.storyapps.ui.AuthenActivity
import com.example.storyapps.ui.dataStore
import com.example.storyapps.ui.maps.MapsActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val pref = UserPreferences.getInstance(dataStore)
        val factory = ViewModelFactory(pref)
        storyViewModel = ViewModelProvider(
            this, factory
        )[StoryViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                storyViewModel.logout()
                val intent = Intent(this, AuthenActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            R.id.addStory_menu -> {
                val navController = findNavController(R.id.fragment_story_container)
                val currentFragment = navController.currentDestination?.id ?: 0
                if (currentFragment != R.id.addStoryFragment) {
                    navController.navigate(R.id.addStoryFragment)
                }
                true
            }
            R.id.maps_menu -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}