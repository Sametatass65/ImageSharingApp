package com.smt.imageshared.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smt.imageshared.adapter.ProfileRecyclerViewAdaptor
import com.smt.imageshared.R
import com.smt.imageshared.model.Post
import com.smt.imageshared.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var postList = ArrayList<Post>()
    private lateinit var viewModel:ProfileViewModel
    private lateinit var recyclerViewAdapter : ProfileRecyclerViewAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        viewModel = ViewModelProvider(this).get(ProfileViewModel :: class.java)
        viewModel.GetData(database)

        GetData()
        val layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        recyclerViewAdapter = ProfileRecyclerViewAdaptor(postList)
        recyclerView2.adapter = recyclerViewAdapter
    }
    fun GetData() {
        viewModel.posts.observe(this, Observer {
            it?.let {
                recyclerViewAdapter.UpdateListPost(it)
            }
        })
    }

}