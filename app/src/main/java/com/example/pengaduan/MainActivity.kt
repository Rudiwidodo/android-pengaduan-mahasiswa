package com.example.pengaduan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            goToLoginActivity()
        }, 3000)
    }

    private fun goToLoginActivity(){
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

}