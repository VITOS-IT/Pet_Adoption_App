package com.example.petadoptionapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.example.petadoptionapp.ChangedEmail
import com.example.petadoptionapp.R
import com.example.petadoptionapp.httpservice.MyPetAppApi
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        setTitle("Change email")

        val APP_PREFERENCES = "mysettings"
        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService
        var mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val changeEmailView = findViewById<TextInputLayout>(R.id.changeEmailInput)
        val confirmButton = findViewById<TextView>(R.id.confirmBtn)
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        confirmButton.setOnClickListener {
            val mail = changeEmailView.editText?.text.toString()
            var maskedEmail = if (mail.isNotEmpty()) mail.lowercase()
            else ""
            val email = ChangedEmail(maskedEmail)
            CoroutineScope(Dispatchers.IO).launch {
                val decodedJsonResult = httpApiService.changeEmail(email)
                val s:String = decodedJsonResult.toString()
                decodedJsonResult.toString()
                withContext(Dispatchers.Main) {
                    var editor: SharedPreferences.Editor = mSettings.edit()
                    editor.putString("email", maskedEmail).apply()
                    dialog.show()
                    val intent = Intent(applicationContext, ProfileActivity::class.java).apply {

                    }
                    startActivity(intent)
                }
            }
        }
    }
}