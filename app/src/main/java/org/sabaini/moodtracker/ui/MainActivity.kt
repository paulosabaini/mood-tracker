package org.sabaini.moodtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.sabaini.moodtracker.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var emojiConfig: BundledEmojiCompatConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MoodTracker)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        // Finding the Navigation Controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setting Navigation Controller with the BottomNavigationView
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(
            navController
        )

        // Configure EmojiCompat
        EmojiCompat.init(emojiConfig)
    }
}