package com.example.petadoptionapp

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_info)

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_screen)
        dialog.setCanceledOnTouchOutside(false)

        val petInfoHeadView: TextView = findViewById(R.id.petInfoHeadView)
        val petInfoNameView: TextView = findViewById(R.id.petInfoNameView)
        val petInfoGenderView: TextView = findViewById(R.id.petInfoGenderView)
        val petInfoVaccinatedView: TextView = findViewById(R.id.petInfoVaccinatedView)
        val petInfoAgeView: TextView = findViewById(R.id.petInfoAgeView)

        val petInfoImgView: ImageView = findViewById(R.id.petInfoImageView)
        val petInfoInterestedBtn: TextView = findViewById(R.id.petInfoInterestedButton)

        val petItem = intent.getSerializableExtra("extra_object") as PetsModel

        petInfoHeadView.text = petItem.name
        petInfoNameView.text = "Name: " + petItem.name
        petInfoGenderView.text = "Gender: none"
        if (petItem.vaccinated == 1)
            petInfoVaccinatedView.text = "Vaccinated: Yes"
        else
            petInfoVaccinatedView.text = "Vaccinated: No"
        if (petItem.age%10 ==1)
            petInfoAgeView.text = "Age: ${petItem.age} yesr"
        else
            petInfoAgeView.text = "Age: ${petItem.age} yesrs"

        petInfoInterestedBtn.text = "I’m interested in ${petItem.name}!"

        val imageUri:String = petItem.url
        Picasso.get().load(imageUri).into(petInfoImgView);


        val myApplication = application as MyPetAppApi
        val httpApiService = myApplication.httpApiService


        petInfoInterestedBtn.setOnClickListener {
            dialog.show()


            val petId = PetIdClass(petItem.id)


            CoroutineScope(Dispatchers.IO).launch {

                try {
                    val decodedJsonResult = httpApiService.createNewPetInterest(petId)

                    withContext(Dispatchers.Main) {


                        Toast.makeText(applicationContext, "Pet add to interested", Toast.LENGTH_SHORT).show()
                        Log.d("Add pet Exception: ", decodedJsonResult.toString())

                        dialog.dismiss()

                         }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {

                        Log.d("Add pet Exception: ", e.message.toString())
                        dialog.dismiss()

                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }
    }
}