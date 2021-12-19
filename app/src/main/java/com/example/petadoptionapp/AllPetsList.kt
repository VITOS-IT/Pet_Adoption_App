package com.example.petadoptionapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.Serializable

class AllPetsList : AppCompatActivity() {
    lateinit var petResList :RecyclerView
    private lateinit var binding: ActivityAllPetsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        petResList = findViewById<RecyclerView>(R.id.allPetsView)
//        setContentView(R.layout.activity_all_pets_list)

        setTitle(R.string.petlisttitle)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_humburg_foreground)

        binding = ActivityAllPetsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.profile -> {
                        val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
                        }
                        startActivity(intent)
                    }
                     R.id.interest -> {
                        val intent = Intent(applicationContext, MyInterestedActivity::class.java).apply {
                        }
                        startActivity(intent)
                    }
                    R.id.users -> {
                        val intent = Intent(applicationContext, OtherUsersActivity::class.java).apply {
                        }
                        startActivity(intent)
                    }
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }
        }
        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)

        val email = mSettings.getString("email", "")

// ---------
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val headerTextView = headerView.findViewById<View>(R.id.profile_email) as TextView
        headerTextView.text = email
        val headerImage = headerView.findViewById<View>(R.id.avatarProfile) as Button
        val imageButton = headerView.findViewById<View>(R.id.imageButton) as ImageView
        headerImage.text = email?.get(0).toString()
        headerImage.textSize = 70F

        try {
            val cw = ContextWrapper(applicationContext)
            val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
            val mypath = File(directory, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(mypath))
            if (mypath.exists()){
                imageButton.setImageBitmap(b)
                imageButton.setBackgroundColor(Color.TRANSPARENT)
                headerImage.text = ""
                headerImage.background = null
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

//        ----------
        CoroutineScope(Dispatchers.IO).launch {
            val token = mSettings.getString("token", "")!!
            val decodedJsonResult = httpApiService.getAllPets("Bearer ${token}")

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
            petResList = findViewById<RecyclerView>(R.id.allPetsView)
            val linearLayoutManager = LinearLayoutManager(applicationContext)
//            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            petResList.layoutManager = linearLayoutManager
            val adapter = PetListAdapter(pets, object : OnItemClickListener {
                override fun onClicked(item: PetsModel) {
                    val intent = Intent(applicationContext, PetInfoActivity::class.java)
                    intent.putExtra("extra_object", item as Serializable)
                    startActivity(intent)
//                    Toast.makeText(this@AllPetsList, "tag = ${item.name}", Toast.LENGTH_SHORT).show()
                }
            })
            petResList.adapter = adapter
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawer.isDrawerOpen(GravityCompat.START))
                    binding.drawer.closeDrawer(GravityCompat.START)
                else binding.drawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return true
    }
}