package com.smt.imageshared

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_image_shared.*
import java.sql.Timestamp
import java.util.*

class ImageSharedActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseFirestore
    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_shared)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun Share (view : View){

        val reference = storage.reference
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"
        val imageReference = reference.child("images").child(imageName)

        if (selectedImage != null){

            imageReference.putFile(selectedImage!!).addOnSuccessListener  {taskSnapstop->
                val storageUri = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                storageUri.downloadUrl.addOnCompleteListener { uri->
                    val selectedImageUri = uri.toString()
                    val currentUserEmail = auth.currentUser?.email.toString()
                    val commentUser = commentText.text.toString()
                    val transactionTime = com.google.firebase.Timestamp.now()

                    val postHasMap = hashMapOf<String,Any>()
                    postHasMap.put("userEmail", currentUserEmail)
                    postHasMap.put("userComment", commentUser)
                    postHasMap.put("imageUri",selectedImageUri)
                    postHasMap.put("transactionTime",transactionTime)

                    database.collection("Post").add(postHasMap).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener{exception ->
                        Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                if(exception != null){
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun ImageAdd(view : View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent ,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode == 1){
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent ,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selectedImage = data.data

            if (selectedImage != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,selectedImage!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(selectedBitmap)
                }else{
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    imageView.setImageBitmap(selectedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }




















}