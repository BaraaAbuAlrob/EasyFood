package com.baraa.myprojects.easyfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baraa.myprojects.easyfood.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        Toast.makeText(applicationContext, "Checking Your Internet Connection...", Toast.LENGTH_LONG).show()

        val thread = Thread {
            try {
                Thread.sleep(2600)
                val runSplash = Intent(applicationContext, MainActivity::class.java)
                startActivities(arrayOf(runSplash))
                finish()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        thread.start()
    }
}