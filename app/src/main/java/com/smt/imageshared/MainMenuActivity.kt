package com.smt.imageshared

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database :FirebaseFirestore
    private lateinit var recyclerViewAdaptor : RecyclerViewAdapter
    var postList = ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        GetData()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdaptor = RecyclerViewAdapter(postList)
        recyclerView.adapter = recyclerViewAdaptor

    }

    fun GetData() {
        database.collection("Post").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (value != null)
                    if (!value.isEmpty) {
                        val documents = value.documents
                        postList.clear()
                        for (document in documents) {
                            val userEmail = document.get("userEmail") as String
                            val userComment = document.get("userComment") as String
                            val imageUri = document.get("imageUri") as String
                            val dowloadPost = Post(userEmail,userComment,imageUri)
                            postList.add(dowloadPost)
                        }
                        recyclerViewAdaptor.notifyDataSetChanged()
                    }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.imageAdd){
            val intent = Intent(this , ImageSharedActivity :: class.java)
            startActivity(intent)

        }else if (item.itemId == R.id.profile){
            val intent = Intent(this , ProfileActivity :: class.java)
            startActivity(intent)

        }else if (item.itemId == R.id.logOut){
            auth.signOut()
            val intent = Intent(this , LogInActivity :: class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}