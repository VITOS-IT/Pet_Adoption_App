package com.example.petadoptionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class OtherUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_users)

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        CoroutineScope(Dispatchers.IO).launch {

            val decodedJsonResult = httpApiService.getUsers()

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