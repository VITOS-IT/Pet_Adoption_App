package com.example.petadoptionapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.example.petadoptionapp.R
import com.example.petadoptionapp.httpservice.MyPetAppApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)
        setTitle("Confirm account delete")

        val APP_PREFERENCES = "mysettings"
        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService
        var mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val email = mSettings.getString("email", "")
        val accountDetailsView = findViewById<TextView>(R.id.accountDetails)
        accountDetailsView.text = "Account with email \n $email \n will be permanently deleted"
        val confirmDeleteButton = findViewById<TextView>(R.id.confirmDelete)
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        confirmDeleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                httpApiService.deleteAcc()

                withContext(Dispatchers.Main) {
                    mSettings.edit().clear().apply()
                    dialog.show()
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {

                    }
                    startActivity(intent)
                }
            }
        }
    }
}