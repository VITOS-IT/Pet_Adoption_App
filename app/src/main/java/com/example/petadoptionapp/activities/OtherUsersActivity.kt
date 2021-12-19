package com.example.petadoptionapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoptionapp.R
import com.example.petadoptionapp.UserData
import com.example.petadoptionapp.adapters.UsersAdapter
import com.example.petadoptionapp.httpservice.MyPetAppApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtherUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_users)

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService
        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        val token = mSettings.getString("token", "")!!
        CoroutineScope(Dispatchers.IO).launch {

            val decodedJsonResult = httpApiService.getUsers("Bearer ${token}")

            var userList = ArrayList<UserData>()

            withContext(Dispatchers.Main) {

                if (decodedJsonResult.isSuccessful) {
                    val items = decodedJsonResult.body()?.users
                    if (items != null) {
                        for (i in 0 until items.count()) {
                            val user = UserData(
                                items[i].email,
                                items[i].reservationsAt
                            )
                            userList.add(user)
                        }
                        createlist(userList)
                    } else {
                        Log.e("RETROFIT_ERROR", decodedJsonResult.code().toString())
                    }
                }
            }
        }
    }

    fun createlist(users: ArrayList<UserData>) {
        val usersRecyclerView = findViewById<RecyclerView>(R.id.otherUsers)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        usersRecyclerView.layoutManager = linearLayoutManager
        val adapter = UsersAdapter(users)
        usersRecyclerView.adapter = adapter
    }
}