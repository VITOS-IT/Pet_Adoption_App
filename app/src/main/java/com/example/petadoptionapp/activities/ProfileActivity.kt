package com.example.petadoptionapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.sql.Timestamp
import java.util.*
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.widget.ImageButton
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.petadoptionapp.R
import java.io.*

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.profileTitle)
        setContentView(R.layout.activity_profile)

        val avatar = findViewById<Button>(R.id.avatarProfile)
        val profileEmail = findViewById<TextView>(R.id.profile_email)
        val memberSinceView = findViewById<TextView>(R.id.memberSince)
        val logOut = findViewById<TextView>(R.id.logOut)
        val delAcc = findViewById<TextView>(R.id.delAcc)
        val changeEmail = findViewById<TextView>(R.id.changeEmail)
        val history = findViewById<TextView>(R.id.history)
        val imageButton = findViewById<ImageButton>(R.id.imageButton)

        var mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        val email = mSettings.getString("email", "")
        val memberSinceLong = mSettings.getLong("memberSince", 0L)
        profileEmail.text = email
        avatar.text = email?.get(0).toString()
        avatar.textSize = 70F

        try {
            val cw = ContextWrapper(applicationContext)
            val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
            val mypath = File(directory, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(mypath))
            if (mypath.exists()){
                imageButton.setImageBitmap(b)
                imageButton.setBackgroundColor(Color.TRANSPARENT)
                avatar.text = ""
                avatar.background = null
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


        avatar.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(this, "edu.stanford.rkpandey.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        memberSinceView.text = "Member since: ${getDateTime(memberSinceLong)}"

        logOut.setOnClickListener {
            mSettings.edit().clear().apply()
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
            }
            startActivity(intent)
        }

        changeEmail.setOnClickListener {
            val intent = Intent(applicationContext, ChangeEmailActivity::class.java).apply {
            }
            startActivity(intent)
        }

        delAcc.setOnClickListener {
            val intent = Intent(applicationContext, DeleteAccountActivity::class.java).apply {
            }
            startActivity(intent)
        }

        history.setOnClickListener {
            val intent = Intent(applicationContext, LoginHistoryActivity::class.java).apply {
            }
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, AllPetsList::class.java)
        startActivity(intent)
    }
    
    private fun getDateTime(s: Long): Date {
        try {
            val stamp = Timestamp(s)
            return Date(stamp.time)
        } catch (e: Exception) {
            print(e)
            return Date()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            saveToInternalStorage(takenImage)
            val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
            }
            startActivity(intent)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fos != null) {
                    fos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }
}