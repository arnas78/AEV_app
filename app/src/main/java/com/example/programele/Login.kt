package com.example.programele

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()
    lateinit var loginTextInputEditTextPassword: EditText
    lateinit var loginTextInputEditTextEmail: EditText
    lateinit var forgotPass: TextView
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
        forgotPass = findViewById(R.id.login_forgotPass)


        buttonRegister.setOnClickListener {
            val intent = Intent(this, SignUp::class.java);
            startActivity(intent)
        }

        buttonLog.setOnClickListener {
            login(it)
        }

        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java);
            startActivity(intent)
        }

        Paper.init(this)

        val firebaseUsername = Paper.book().read<String>(Prevalent.username)
        val firebasePassword = Paper.book().read<String>(Prevalent.password)
        Log.d(TAG, "usern $firebaseUsername")
        if (!firebaseUsername.isNullOrEmpty() && !firebasePassword.isNullOrEmpty()) {
            if (!TextUtils.isEmpty(firebaseUsername) && !TextUtils.isEmpty(firebasePassword)) {
                Toast.makeText(this, "Palaukite, Jūs jau esate prisijungę...", Toast.LENGTH_SHORT).show()
                auth.signInWithEmailAndPassword(firebaseUsername, firebasePassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }




        fun AllowAccessToAccount(userUsername: String, userPassword: String) {
            if (login_rememberMe.isChecked) {
                Paper.book().write(Prevalent.username, userUsername)
                Paper.book().write(Prevalent.password, userPassword)
            }
        }


        fun login(view: View) {


            val email = loginTextInputEditTextEmail.text.toString()
            val password = loginTextInputEditTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                AllowAccessToAccount(email, password)
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
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
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

}

