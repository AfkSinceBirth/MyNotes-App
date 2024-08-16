package com.example.mynotespracticebuild

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(1000)
            val auth: FirebaseAuth = Firebase.auth
            val user = auth.currentUser

            if(isNetworkAvailable(this@SplashScreen)){
                if(user == null) {
                    val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            else{
                Toast.makeText(this@SplashScreen,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

    fun isNetworkAvailable(context : Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo  = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}