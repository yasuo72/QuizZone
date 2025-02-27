package com.example.quizwiz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizwiz.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class QuizSplashActivity : AppCompatActivity() {
    private lateinit var zoomImageView:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        zoomImageView = findViewById(R.id.splash_imageView)
        animateZoomOut()
    }

    private fun animateZoomOut() {
        zoomImageView.animate()
            .scaleX(0.4f)
            .scaleY(0.4f)
            .setDuration(1000)
            .withEndAction {
                animateZoomIn()
            }
            .start()
    }

    private fun animateZoomIn() {
        zoomImageView.animate()
            .scaleX(30.0f)
            .scaleY(30.0f)
            .setDuration(500)
            .withEndAction {
                // Start the new activity here
                startNewActivity()
            }
            .start()
    }

    private fun startNewActivity() {
        val auth = Firebase.auth
        if (auth.currentUser!= null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        else
        {
            startActivity(Intent(this,GetStartedActivity::class.java))
        }

        finish()
    }
}