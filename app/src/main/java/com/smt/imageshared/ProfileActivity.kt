package com.smt.imageshared

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var postList = ArrayList<Post>()
    private lateinit var recyclerViewAdapter : ProfileRecyclerViewAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        GetData()
        val layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        recyclerViewAdapter = ProfileRecyclerViewAdaptor(postList)
        recyclerView2.adapter = recyclerViewAdapter
    }
    fun GetData(){
        database.collection("Post").addSnapshotListener { value, error ->
            if(error != null ){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        postList.clear()
                        for (document in documents) {
                            val userEmail = document.get("userEmail") as String
                            val userComment = document.get("userComment") as String
                            val imageUri = document.get("imageUri") as String
                            val dowloadPost = Post(userEmail,userComment,imageUri)
                            postList.add(dowloadPost)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}