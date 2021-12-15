package com.example.petadoptionapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import com.example.petadoptionapp.databinding.ActivityAllPetsListBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.util.stream.Collectors.toList

class AllPetsList : AppCompatActivity() {
    private lateinit var binding: ActivityAllPetsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_all_pets_list)
        setTitle(R.string.petlisttitle)
        binding = ActivityAllPetsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            navView.setNavigationItemSelectedListener {
//                when(it.itemId){
//                    R.id.profile -> {
//                        val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
//                        }
//                        startActivity(intent)
//                    }
//                    R.id.interest -> {
//                        val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
//                        }
//                        startActivity(intent)
//                    }
//                    R.id.users -> {
//                        val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
//                        }
//                        startActivity(intent)
//                    }
//                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }
        }

        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)

        val email = mSettings.getString("email", "")
        val token = mSettings.getString("token", "")!!
// ---------
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val headerTextView = headerView.findViewById<View>(R.id.header_email) as TextView
        headerTextView.text = email
        val headerImage = headerView.findViewById<View>(R.id.imageHeader) as Button
        headerImage.text = email?.get(0).toString()
        headerImage.textSize = 70F

//        ----------
        CoroutineScope(Dispatchers.IO).launch {

            val decodedJsonResult = httpApiService.getAllPets()

            var petsList = ArrayList<PetsModel>()

            withContext(Dispatchers.Main) {

                if (decodedJsonResult.isSuccessful) {

                    val items = decodedJsonResult.body()?.pets
                    if (items != null) {
                        for (i in 0 until items.count()) {
                            val pet = PetsModel(items[i].id,items[i].name,items[i].url,items[i].type,items[i].age,items[i].vaccinated)
                            petsList.add(pet)
                        }
                        createlist(petsList)

                    } else {

                        Log.e("RETROFIT_ERROR", decodedJsonResult.code().toString())

                    }

                }
            }
        }
    }

        fun createlist(pets: ArrayList<PetsModel>) {
            val petResList = findViewById<RecyclerView>(R.id.allPetsView)
            val linearLayoutManager = LinearLayoutManager(applicationContext)
//            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            petResList.layoutManager = linearLayoutManager
            val adapter = PetListAdapter(pets, object : OnItemClickListener {
                override fun onClicked(item: PetsModel) {
                    val intent = Intent(applicationContext, PetInfoActivity::class.java)
                    intent.putExtra("extra_object", item as Serializable)
                    startActivity(intent)
                    Toast.makeText(this@AllPetsList, "tag = ${item.name}", Toast.LENGTH_SHORT).show()
                }
            })
            petResList.adapter = adapter
        }
    }