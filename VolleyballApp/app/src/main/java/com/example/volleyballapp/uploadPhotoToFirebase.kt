package com.example.volleyballapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class uploadPhotoToFirebase : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var btnBrowse: Button
    private lateinit var btnUpload: Button
    private lateinit var uri: Uri

    private var storageRef = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo_to_firebase)

        storageRef = FirebaseStorage.getInstance()

        image = findViewById(R.id.imageView)
        btnBrowse = findViewById(R.id.btnBrowse)
        btnUpload = findViewById(R.id.btnUpload)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
            image.setImageURI(it)
            if (it != null) {
                uri = it
            }
        })

        btnBrowse.setOnClickListener({
            galleryImage.launch("image/*")
        })

        btnUpload.setOnClickListener({
            storageRef.getReference("images").child(System.currentTimeMillis().toString()).putFile(uri).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val mapImage = mapOf(
                        "url" to it.toString()
                    )

                    val databaseRefrence = FirebaseDatabase.getInstance().getReference("userImages")
                    databaseRefrence.child(userId).setValue(mapImage).addOnSuccessListener {
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                        .addOnFailureListener({ error ->
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        })
                }
            }
        })
    }
}