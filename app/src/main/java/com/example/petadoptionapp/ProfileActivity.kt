package com.example.petadoptionapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.sql.Timestamp
import java.util.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.profileTitle)
        setContentView(R.layout.activity_profile)

        val avatar = findViewById<Button>(R.id.avatarProfile)
        val profileEmail = findViewById<TextView>(R.id.profile_email)
        val memberSinceView = findViewById<TextView>(R.id.memberSince)
        val logOut = findViewById<TextView>(R.id.logOut)
        val delAcc = findViewById<TextView>(R.id.delAcc)
        val changeEmail = findViewById<TextView>(R.id.changeEmail)
        val history = findViewById<TextView>(R.id.history)

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        val email = mSettings.getString("email", "")
        val memberSinceLong = mSettings.getLong("memberSince", 0L)
        profileEmail.text = email
        avatar.text = email?.get(0).toString()
        avatar.textSize = 70F

        memberSinceView.text = "Member since: ${getDateTime(memberSinceLong)}"

        logOut.setOnClickListener {
            mSettings.edit().clear().apply()
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
            }
            startActivity(intent)
        }

        changeEmail.setOnClickListener {
            val intent = Intent(applicationContext, ChangeEmailActivity::class.java).apply {
            }
            startActivity(intent)
        }

        delAcc.setOnClickListener {
            val intent = Intent(applicationContext, DeleteAccountActivity::class.java).apply {
            }
            startActivity(intent)
        }

        history.setOnClickListener {
//            val intent = Intent(applicationContext, LoginHistory::class.java).apply {
//            }
//            startActivity(intent)
        }

    }
    
    private fun getDateTime(s: Long): Date {
        try {
            val stamp = Timestamp(s)
            return Date(stamp.time)
        } catch (e: Exception) {
            print(e)
            return Date()
        }
    }
}