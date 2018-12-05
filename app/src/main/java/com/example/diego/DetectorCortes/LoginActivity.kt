package com.example.diego.DetectorCortes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

//Creditos a https://www.youtube.com/watch?v=ihJGxFu2u9Q&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-
//y a Brian Voong :)

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_ID.setOnClickListener {
            val email = email_loginActivity_ID.text.toString()
            val password = password_loginActivity_ID.text.toString()

            Log.d("LoginActivity", "Attempting to login with email/password :$email")

            //Firebase Login
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                //else if successful
                Log.d("LoginActivity", "Successfully logged in with uid: ${it.result?.user?.uid}")
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener{
                Log.d("LoginActivity", "Failed to login user: ${it.message}")
                Toast.makeText(this, "Failed to login user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        dont_have_account_ID.setOnClickListener {
            Log.d("LoginActivity", "Try to show RegisterActivity")

            //launch the register somehow
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}