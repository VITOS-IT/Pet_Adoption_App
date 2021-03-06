package com.example.petadoptionapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.petadoptionapp.R
import com.example.petadoptionapp.data.User
import com.example.petadoptionapp.httpservice.MyPetAppApi
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val APP_PREFERENCES = "mysettings"

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        var mSettings = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        val emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val passLayout = findViewById<TextInputLayout>(R.id.passwordLayout)


        val loginBtn = findViewById<TextView>(R.id.loginButton)
        val registerBtn = findViewById<TextView>(R.id.redirectToRegister)

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        loginBtn.setOnClickListener {
            val email = emailLayout.editText?.text.toString()
            val password = passLayout.editText?.text?.toString()
            dialog.show()

            var maskedEmail = if (email.isNullOrEmpty()) ""
            else email.lowercase()
            var maskedPass = if (password.isNullOrEmpty()) ""
            else password.lowercase()
            CoroutineScope(Dispatchers.IO).launch {
                val loginUser = User(maskedEmail, maskedPass)
                try {
                    val decodedJsonResult = httpApiService.login(loginUser)
                    withContext(Dispatchers.Main) {

                        val editor:SharedPreferences.Editor = mSettings.edit()
                        editor.putString("token", decodedJsonResult.token.toString()).apply()
                        editor.putString("email", decodedJsonResult.email.toString()).apply()
                        editor.putLong("memberSince", decodedJsonResult.memberSince).apply()
                        val intent = Intent(applicationContext, AllPetsList::class.java).apply {

                        }

                        dialog.dismiss()
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Login Exception: ", e.localizedMessage)
                        dialog.dismiss()

                        Toast.makeText(
                            applicationContext,
                            "Incorrect email or password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        registerBtn.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java).apply { }
            startActivity(intent)
        }
    }
}