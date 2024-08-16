package com.example.mynotespracticebuild

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passEditText: EditText
    lateinit var loginButton: Button
    lateinit var signUpTextView: TextView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailId)
        passEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.Button1)
        signUpTextView = findViewById(R.id.textView4)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener{
            loginAccount()
        }

        signUpTextView.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun loginAccount(){
        var email : String = emailEditText.getText().toString()
        var pass : String = passEditText.getText().toString()
        var isValid : Boolean = validateInput(email, pass)
        if(!isValid) return
        loginAccountInFirebase(email, pass)
    }

    fun loginAccountInFirebase(email: String, pass: String){
        changeInProgress(true)
        val auth : FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this) {task->
            changeInProgress(false)
            if(task.isSuccessful){
                Toast.makeText(this@LoginActivity,"LogIn Successfull",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@LoginActivity,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeInProgress(showProgress : Boolean){
        if(showProgress){
            progressBar.visibility = View.VISIBLE
            loginButton.visibility = View.INVISIBLE
        }
        else{
            progressBar.visibility = View.INVISIBLE
            loginButton.visibility = View.VISIBLE
        }
    }

    fun validateInput(email : String, pass: String) : Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid")
            return false
        }
        if(pass.length<8){
            passEditText.setError("Password should contain minimum 8 characters")
            return false
        }

        return true
    }
}