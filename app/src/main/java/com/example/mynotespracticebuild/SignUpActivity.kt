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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    lateinit var emailEditText : EditText
    lateinit var passEditText : EditText
    lateinit var confirmPassEditText : EditText
    lateinit var createAccountButton : Button
    lateinit var loginTextView : TextView
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailId)
        passEditText = findViewById(R.id.password)
        confirmPassEditText = findViewById(R.id.confirmPassword)

        createAccountButton = findViewById(R.id.Button1)
        loginTextView = findViewById(R.id.textView4)
        progressBar = findViewById(R.id.progressBar)

        createAccountButton.setOnClickListener {
            createAccount()
        }
        loginTextView.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun createAccount(){
        var email : String = emailEditText.getText().toString()
        var pass : String = passEditText.getText().toString()
        var confirmPass : String = confirmPassEditText.getText().toString()
        var isValid : Boolean = validateInput(email, pass, confirmPass)
        if(!isValid) return
        createAccountInFirebase(email, pass)

    }

    fun createAccountInFirebase(email: String, pass: String){
        changeInProgress(true)
        val auth: FirebaseAuth = Firebase.auth

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this) {task->
            changeInProgress(false)
            if(task.isSuccessful){
                Toast.makeText(this, "Account created successfully. Verify email",Toast.LENGTH_SHORT).show()
                auth.currentUser?.sendEmailVerification()
                auth.signOut()
                finish()
            }
            else{
                Toast.makeText(this, task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeInProgress(showProgress : Boolean){
        if(showProgress){
            progressBar.visibility = View.VISIBLE
            createAccountButton.visibility = View.INVISIBLE
        }
        else{
            progressBar.visibility = View.INVISIBLE
            createAccountButton.visibility = View.VISIBLE
        }
    }

    fun validateInput(email : String, pass: String, confirmPass: String) : Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid")
            return false
        }
        if(pass.length<8){
            passEditText.setError("Password should contain atleast 8 characters")
            return false
        }
        if(!confirmPass.equals(pass)){
            confirmPassEditText.setError("The password and confirm password do not match.")
            return false
        }

        return true
    }
}