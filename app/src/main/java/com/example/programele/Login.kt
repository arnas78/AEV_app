package com.example.programele

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings

class Login : AppCompatActivity() {

    lateinit var loginTextInputEditTextPassword: EditText
    lateinit var loginTextInputEditTextEmail: EditText
    lateinit var signUpText: TextView
    lateinit var buttonLog: Button
    lateinit var buttonRegister: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

//        textInputEditTextUsername = findViewById(R.id.usernameSignUp)
        loginTextInputEditTextPassword = findViewById(R.id.passwordLogin)
        loginTextInputEditTextEmail = findViewById(R.id.usernameLogin)
        buttonRegister = findViewById(R.id.login_btnRegister)
        buttonLog = findViewById(R.id.login_btnLogin)
        signUpText = findViewById(R.id.login_signUpText)


        buttonRegister.setOnClickListener{
            val intent = Intent(this, SignUp::class.java);
            startActivity(intent)
        }

        buttonLog.setOnClickListener{
            login(it)
        }
//        login_btnForgotPassword.setOnClickListener{
//            val intent = Intent(this, ActivityForgotPassword::class.java);
//            startActivity(intent)
//
//        }
    }
    fun login(view: View) {
        val email = loginTextInputEditTextEmail.text.toString()
        val password = loginTextInputEditTextPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty())
        {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        else {
            Toast.makeText(
                applicationContext, "Email and password fields cannot be empty",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    private fun buildActionCodeSettings() {
        // [START auth_build_action_code_settings]
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */)
        }
        // [END auth_build_action_code_settings]
    }



}



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
////import com.vishnusivadas.advanced_httpurlconnection.PutData
//
//class Login : AppCompatActivity() {
//
//    lateinit var textInputEditTextUsername: TextInputEditText
//    lateinit var textInputEditTextPassword: TextInputEditText
//    lateinit var buttonLogin: Button
//    lateinit var textViewSignUp: TextView
//    lateinit var progressBar: ProgressBar
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        textInputEditTextUsername = findViewById(R.id.usernameLogin)
//        textInputEditTextPassword = findViewById(R.id.passwordLogin)
//        buttonLogin = findViewById(R.id.buttonLogin)
//        textViewSignUp = findViewById(R.id.signUpText)
//        progressBar = findViewById(R.id.progress)
//
//        textViewSignUp.setOnClickListener(View.OnClickListener {
//            val intent = Intent(applicationContext, SignUp::class.java)
//            startActivity(intent)
//            finish()
//        })
//
//        buttonLogin.setOnClickListener(View.OnClickListener {
//            val username: String
//            val password: String
//            username = textInputEditTextUsername.getText().toString()
//            password = textInputEditTextPassword.getText().toString()
//            if (username != "" && password != "") {
//                progressBar.setVisibility(View.VISIBLE)
//                //Start ProgressBar first (Set visibility VISIBLE)
//                val handler = Handler(Looper.getMainLooper())
//                handler.post {
//                    //Starting Write and Read data with URL
//                    //Creating array for parameters
//                    val field = arrayOfNulls<String>(2)
//                    field[0] = "username"
//                    field[1] = "password"
//                    //Creating array for data
//                    val data = arrayOfNulls<String>(2)
//                    data[0] = username
//                    data[1] = password
//
//                    val putData = PutData(
//                        "https://stud.if.ktu.lt/~lukmar7/LoginRegister/login.php",
//                        "POST",
//                        field,
//                        data
//                    )
//                    if (putData.startPut()) {
//                        if (putData.onComplete()) {
//                            progressBar.setVisibility(View.GONE)
//                            val result = putData.result
//                            if (result == "Login Success") {
//                                val intent = Intent(applicationContext, MainActivity::class.java)
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
//    }
//}