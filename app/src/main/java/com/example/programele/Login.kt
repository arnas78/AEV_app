package com.example.programele

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()
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

        val loginbutton=findViewById<LoginButton>(R.id.login_button)

        loginbutton.setOnClickListener {
            if (userLoggedIn()) {
                auth.signOut()
            }
            else {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("public_profile"))
            }
        }
        // Initialize Facebook Login button



        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
        // ...

//        textInputEditTextUsername = findViewById(R.id.usernameSignUp)

//        login_btnForgotPassword.setOnClickListener{
//            val intent = Intent(this, ActivityForgotPassword::class.java);
//            startActivity(intent)
//
//        }
    }

    private fun userLoggedIn(): Boolean {
        return auth.currentUser!=null && !AccessToken.getCurrentAccessToken()!!.isExpired
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            for (userInfo in currentUser.providerData) {
//                if (userInfo.providerId == "facebook.com") {
//                    Log.d("TAG", "User is signed in with Facebook")
//                }
//            }
//        }
//    }




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