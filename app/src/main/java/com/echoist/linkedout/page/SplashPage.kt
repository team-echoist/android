package com.echoist.linkedout.page

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.media3.exoplayer.ExoPlayer


class SplashPage : AppCompatActivity() {
    private var keepSplash = true
    private val delay = 1200L
    lateinit var player : ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        setupSplashScreen(splashScreen)

        val intent = Intent(
            this,
            LoginPage::class.java
        )
        startActivity(intent)
        finish()
    }
    private fun setupSplashScreen(splashScreen: SplashScreen) {
        // Replace this timer with your logic to load data on the splash screen.
        splashScreen.setKeepOnScreenCondition { keepSplash }
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplash = false
        }, delay)
    }
}