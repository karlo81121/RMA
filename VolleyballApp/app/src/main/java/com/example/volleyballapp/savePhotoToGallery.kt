package com.example.volleyballapp
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream


class savePhotoToGallery : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private val CAMERA_PERMISSION_CODE = 101

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_photo_to_gallery)

        imageView = findViewById(R.id.imageView)
        val btnCapture = findViewById<Button>(R.id.btnCapture)
        val saveButton = findViewById<Button>(R.id.saveButton)

        btnCapture.setOnClickListener {
            checkAndRequestCameraPermission()
        }

        saveButton.setOnClickListener {
            saveImageToGallery()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun saveImageToGallery() {
        val imageBitmap = (imageView.drawable as BitmapDrawable).bitmap

        val imageFileName = "IMG_${System.currentTimeMillis()}.jpg"
        val storageDir = getExternalFilesDir(null)

        try {
            val imageFile = File(storageDir, imageFileName)
            val outputStream = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Update the media scanner to add the image to the gallery
            MediaStore.Images.Media.insertImage(contentResolver, imageFile.absolutePath, imageFile.name, null)

            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, uploadPhotoToFirebase::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            // Permission has already been granted
            // You can proceed with capturing the image
            dispatchTakePictureIntent()
        }
    }

    // Call this function where you want to request the camera permission
    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            // Permission has already been granted
            // You can proceed with capturing the image
            dispatchTakePictureIntent()
        }
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with capturing the image
                dispatchTakePictureIntent()
            } else {
                // Permission denied, handle it (show a message, disable functionality, etc.)
                // You may want to inform the user about why the permission is needed.
            }
        }
    }
}
