package com.example.petadoptionapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class LoginHistoryActivity : AppCompatActivity() {

    lateinit var historyResList :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Login history")
        setContentView(R.layout.activity_login_history)

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)

        val memberSinceLong = mSettings.getLong("memberSince", 0L)
        val memberView = findViewById<TextView>(R.id.memberSinceView)
        memberView.text = "Member since: ${getDateTime(memberSinceLong)}"

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        CoroutineScope(Dispatchers.IO).launch {
            val decodedJsonResult = httpApiService.getHistory()

            var historyList = ArrayList<Long>()

            withContext(Dispatchers.Main) {

                if (decodedJsonResult.isSuccessful) {

                    val items = decodedJsonResult.body()?.loginEntries
                    if (items != null) {
                        for (it in items) {
                            historyList.add(it.loginTimestamp)
                        }
                        createlist(historyList)
                    } else {
                        Log.e("RETROFIT_ERROR", decodedJsonResult.code().toString())
                    }
                }
            }
        }

    }

    fun createlist(history: ArrayList<Long>) {
        historyResList = findViewById<RecyclerView>(R.id.loginHistoryListView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        historyResList.layoutManager = linearLayoutManager
        val adapter = LoginHistoryAdapter(history)
        historyResList.adapter = adapter
    }

    private fun getDateTime(s: Long): String {
        try {
            val pattern = "dd MMM yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)

            return simpleDateFormat.format(s)
        } catch (e: Exception) {
            print(e)
            return ""
        }
    }
}

