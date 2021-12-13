package com.example.petadoptionapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllPetsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_pets_list)

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)

        val textView = findViewById<TextView>(R.id.emailView)
        val email = mSettings.getString("email", "")
        val token = mSettings.getString("token", "")!!
        textView.text = token

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val decodedJsonResult = httpApiService.getAllPets(token)
                val petList = decodedJsonResult.toList()

                var pets = ArrayList<String>()
                if (petList.size != 0)
                    for (item in petList)
                        pets.add("Result: " + item.name)
                else
                    pets.add("Something went wrong")
                withContext(Dispatchers.Main) {

                    createlist(pets)
                }
            }catch (e:Exception){
                Log.d("Register Exception: ", e.message.toString())
            }
        }
    }

    fun createlist(pets: ArrayList<String>) {
        val petResList = findViewById<RecyclerView>(R.id.allPetsView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        petResList.layoutManager = linearLayoutManager
        val adapter = PetListAdapter(pets, object : OnItemClickListener {
            override fun onClickek(tag: String) {
                Toast.makeText(this@AllPetsList, "tag = ${tag}", Toast.LENGTH_SHORT).show()
            }
        })
        petResList.adapter = adapter
    }
}