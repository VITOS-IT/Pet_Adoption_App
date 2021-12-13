package com.example.petadoptionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val intent = Intent(applicationContext, MainActivity::class.java).apply {

        }

        Timer("SettingUp", false).schedule(2000) {
            startActivity(intent)
            finish()

        }
    }
}