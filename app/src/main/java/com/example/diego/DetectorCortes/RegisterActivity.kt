package com.example.diego.DetectorCortes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

//Creditos a https://www.youtube.com/watch?v=ihJGxFu2u9Q&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-
//y a Brian Voong :)

class RegisterActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_ID.setOnClickListener {
            performRegisterOnFirebase()
        }

        already_have_account_ID.setOnClickListener {
            Log.d("RegisterActivity", "Try to show LoginActivity")

            //launch the login somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegisterOnFirebase(){
        val email = email_registerActivity_ID.text.toString()
        val password = password_registerActivity_ID.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the blanks correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is" + email)
        Log.d("RegisterActivity", "Password is: $password")

        //Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(!it.isSuccessful) return@addOnCompleteListener
            //else if successful
            //Log.d("RegisterActivity", "Successfully created user with uid: ${it.result.user.uid}")
            saveUserToFirebaseDatabase()
        }.addOnFailureListener{
            Log.d("RegisterActivity", "Failed to create user: ${it.message}")
            Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

        }
    }

    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_registerActivity_ID.text.toString())

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
                    //if you dont add this, when you press the back button you will go back to the register
                    //activity, but by adding this you will go back to the menu screen of the phone
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to set value to database: ${it.message}")
                }
    }

}



