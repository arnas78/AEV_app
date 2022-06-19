package com.example.programele

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgot_btnSend.setOnClickListener()
        {
            resetPassword()
        }
        forgot_btnBack.setOnClickListener()
        {
            val intent = Intent(this, Login::class.java);
            startActivity(intent)
        }
    }

    fun resetPassword() {
        val email = forgot_editEmail.text.toString()
        if (email.isNotEmpty()) {
            Firebase.auth.sendPasswordResetEmail("arnasklimas123@gmail.com")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }
        }
    }
}