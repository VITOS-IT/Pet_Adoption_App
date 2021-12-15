package com.example.petadoptionapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import com.example.implementbooks.OnItemRemoveClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyInterestedActivity : AppCompatActivity() {

    lateinit var dialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("I am interested in …")
        setContentView(R.layout.activity_my_interested)
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        updateInterestList()
    }

    fun updateInterestList() {
        val deniedView = findViewById<TextView>(R.id.notInterestedView)
        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService
        dialog.show()
        CoroutineScope(Dispatchers.IO).launch {

            val decodedJsonResult = httpApiService.getPetInterest()
            val decodedPetsJsonResult = httpApiService.getAllPets()

            var petsList = ArrayList<PetsModel>()
            var petMap:MutableMap<Int, Int> = mutableMapOf()
            withContext(Dispatchers.Main) {

                if (decodedJsonResult.isSuccessful) {

                    val items = decodedJsonResult.body()?.petInterests
                    if (items != null) {
                        if (items.count()>0)
                            deniedView.visibility = View.INVISIBLE
                        else
                            deniedView.visibility = View.VISIBLE

                        for (it in items) {
                            petMap.put(it.petId, it.interestId )
                        }
                        if (decodedPetsJsonResult.isSuccessful) {
                            val itemsPet = decodedPetsJsonResult.body()?.pets
                            if (itemsPet != null) {
                                for (i in 0 until itemsPet.count()) {
                                    if (itemsPet[i].id in petMap.keys) {
                                        val pet = PetsModel(
                                            itemsPet[i].id,
                                            itemsPet[i].name,
                                            itemsPet[i].url,
                                            itemsPet[i].type,
                                            itemsPet[i].age,
                                            itemsPet[i].vaccinated
                                        )
                                        petsList.add(pet)
                                    }
                                }
                                createlist(petsList, petMap)
                            }

                        }
                    } else {
                        deniedView.visibility = View.VISIBLE
                        Log.e("RETROFIT_ERROR", decodedJsonResult.code().toString())
                    }
                }
                dialog.dismiss()
            }
        }
    }

    fun createlist(pets: ArrayList<PetsModel>, petMap: MutableMap<Int,Int>) {
        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService
        val petResList = findViewById<RecyclerView>(R.id.allInterestedPetsView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        petResList.layoutManager = linearLayoutManager
        val adapter = PetInterestListAdapter(pets, object : OnItemRemoveClickListener {
            override fun onClicked(item: Int) {
                dialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        httpApiService.deletePetInterest(petMap[item]!!)
                        withContext(Dispatchers.Main) {
                            updateInterestList()
                            dialog.dismiss()
                        }
                    }catch (e:Exception){
                        dialog.dismiss()
                        Log.e("error ",e.message.toString())
                    }
                }
            }
        })
        petResList.adapter = adapter
    }
}
