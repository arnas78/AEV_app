package com.example.programele

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

//        supportActionBar?.hide()
//
        Handler().postDelayed({
            val intent = Intent(this@LoadingScreenActivity, Login::class.java)
            startActivity(intent)
        }, 3000)

    }
}