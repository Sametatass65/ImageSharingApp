package com.smt.imageshared

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class LogInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser  !=  null){
            val intent = Intent(this,MainMenuActivity :: class.java)
            startActivity(intent)
            finish()
        }
    }

    fun Register(view : View){
        auth.createUserWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if (task.isSuccessful){
                val intent = Intent(this,MainMenuActivity :: class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener{exception ->
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    fun LogIn(view : View){
        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful) {
                val currentUserEmail = auth.currentUser?.email.toString()
                Toast.makeText(this, "WELCOME ${currentUserEmail}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
