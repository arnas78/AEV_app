package com.example.programele

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

private lateinit var auth: FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var registerTextInputEditTextUsername: EditText
    lateinit var registerTextInputEditTextPassword: EditText
    lateinit var registerTextInputEditTextEmail: EditText
    lateinit var buttonSigUp: Button
    lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        registerTextInputEditTextUsername = findViewById(R.id.register_editUsername)
        registerTextInputEditTextPassword = findViewById(R.id.register_editPassword)
        registerTextInputEditTextEmail = findViewById(R.id.register_editEmail)
        buttonSigUp = findViewById(R.id.register_btnRegister)

        buttonSigUp.setOnClickListener {
            register(it)
        }
    }

    fun register(view: View) {
        val email = registerTextInputEditTextEmail.text.toString()
        val username = registerTextInputEditTextUsername.text.toString()
        val password = registerTextInputEditTextPassword.text.toString();
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty())
        {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->

                if (task.isSuccessful) {

                    val db = FirebaseFirestore.getInstance()
                    val authUser = auth.currentUser

                    authUser?.updateProfile(userProfileChangeRequest { displayName = username })
                    sendEmailVerification()

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()

                    val user = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password,
                        "name" to "",
                        "surname" to ""
                    )

                    // Add a new document with an auth generated ID
                    db.collection("users").document(authUser?.uid.toString()).set(user)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            Toast.makeText(
                applicationContext, "All fields must be filled out",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
                // [END send_email_verification]
            }

    }
}





//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import android.widget.Button
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.textfield.TextInputEditText
//import com.vishnusivadas.advanced_httpurlconnection.PutData
//
//class SignUp : AppCompatActivity() {
//
//    lateinit var textInputEditTextFullname: TextInputEditText
//    lateinit var textInputEditTextUsername:TextInputEditText
//    lateinit var textInputEditTextPassword:TextInputEditText
//    lateinit var textInputEditTextEmail:TextInputEditText
//    lateinit var buttonSigUp: Button
//    lateinit var textViewLogin: TextView
//    lateinit var progressBar: ProgressBar
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
//
//        textInputEditTextFullname = findViewById(R.id.fullname)
//        textInputEditTextUsername = findViewById(R.id.usernameSignUp)
//        textInputEditTextPassword = findViewById(R.id.passwordSignUp)
//        textInputEditTextEmail = findViewById(R.id.email)
//        buttonSigUp = findViewById(R.id.buttonSignUp)
//        textViewLogin = findViewById(R.id.loginText)
//        progressBar = findViewById(R.id.progress)
//
//        textViewLogin.setOnClickListener(View.OnClickListener {
//            val intent = Intent(applicationContext, Login::class.java)
//            startActivity(intent)
//            finish()
//        })
//
//        buttonSigUp.setOnClickListener(View.OnClickListener {
//            val fullname: String
//            val username: String
//            val password: String
//            val email: String
//            fullname = textInputEditTextFullname.getText().toString()
//            username = textInputEditTextUsername.getText().toString()
//            password = textInputEditTextPassword.getText().toString()
//            email = textInputEditTextEmail.getText().toString()
//            if (fullname != "" && username != "" && password != "" && email != "") {
//                progressBar.setVisibility(View.VISIBLE)
//                //Start ProgressBar first (Set visibility VISIBLE)
//                val handler = Handler(Looper.getMainLooper())
//                handler.post {
//                    //Starting Write and Read data with URL
//                    //Creating array for parameters
//                    val field = arrayOfNulls<String>(4)
//                    field[0] = "fullname"
//                    field[1] = "username"
//                    field[2] = "password"
//                    field[3] = "email"
//                    //Creating array for data
//                    val data = arrayOfNulls<String>(4)
//                    data[0] = fullname
//                    data[1] = username
//                    data[2] = password
//                    data[3] = email
//
//                    val putData = PutData(
//                        "https://stud.if.ktu.lt/~lukmar7/LoginRegister/signup.php",
//                        "POST",
//                        field,
//                        data
//                    )
//                    if (putData.startPut()) {
//                        if (putData.onComplete()) {
//                            progressBar.setVisibility(View.GONE)
//                            val result = putData.result
//                            if (result == "Sign Up Success") {
//                                val intent = Intent(applicationContext, Login::class.java)
//                                startActivity(intent)
//                                finish()
//                            } else {
//                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        }
//                    }
//                    //End Write and Read data with URL
//                }
//            } else {
//                Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
//
//
//    }
//}