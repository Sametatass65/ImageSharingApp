package com.smt.imageshared.viewmodel

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_image_shared.*
import java.util.*

class ImageSharedViewModel:ViewModel() {



    fun Share (activity: Activity, database: FirebaseFirestore, auth:FirebaseAuth, storage: FirebaseStorage, selectedImage: Uri?, commentText: TextView){

        val reference = storage.reference
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"
        val imageReference = reference.child("images").child(imageName)
        // println("ilk ${imageReference}")


        if (selectedImage != null){
            imageReference.putFile(selectedImage!!).addOnSuccessListener  {
                val storageUri = reference.child("images").child(imageName)
                storageUri.downloadUrl.addOnSuccessListener { uri->
                    println(" uri : ${uri.toString()}")
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
                            return@addOnCompleteListener

                        }
                    }.addOnFailureListener{exception ->
                        Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                if(exception != null){
                    Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}