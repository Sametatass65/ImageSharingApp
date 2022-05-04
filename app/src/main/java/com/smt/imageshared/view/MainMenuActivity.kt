package com.smt.imageshared.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smt.imageshared.*
import com.smt.imageshared.adapter.RecyclerViewAdapter
import com.smt.imageshared.model.Post
import com.smt.imageshared.viewmodel.MainMenuViewModel
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database :FirebaseFirestore

    private lateinit var recyclerViewAdaptor : RecyclerViewAdapter
    private lateinit var viewModel:MainMenuViewModel
    var postList = ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        postList.clear()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        viewModel = ViewModelProvider(this).get(MainMenuViewModel :: class.java)
        viewModel.GetData(database)
        GetData()


        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdaptor = RecyclerViewAdapter(postList)
        recyclerView.adapter = recyclerViewAdaptor

    }

    private fun GetData() {
        postList.clear()
        viewModel.posts.observe(this, Observer {
            it?.let {
                recyclerViewAdaptor.UpdateListPost(it)
            }
        })
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
        }else if (item.itemId == R.id.loaction){
            val intent = Intent(this, MapsActivity :: class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}