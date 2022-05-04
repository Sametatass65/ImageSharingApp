package com.smt.imageshared.viewmodel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smt.imageshared.model.Post

class MainMenuViewModel:ViewModel() {

    val posts = MutableLiveData<ArrayList<Post>>()
    private var postList = arrayListOf<Post>()


    fun GetData(database:FirebaseFirestore) {
        database.collection("Post").orderBy("transactionTime" , Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                } else {
                    if (value != null)
                        if (!value.isEmpty) {
                            val documents = value.documents
                            for (document in documents) {
                                val userEmail = document.get("userEmail") as String
                                val userComment = document.get("userComment") as String
                                val imageUri = document.get("imageUri") as String
                                val dowloadPost = Post(userEmail,userComment,imageUri)
                                postList.add(dowloadPost)
                            }
                            if(postList != null){
                                posts.value = postList
                            }

                        }
                }
            }
    }
}