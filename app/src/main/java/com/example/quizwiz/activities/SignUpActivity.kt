package com.example.quizwiz.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.quizwiz.constants.Base
import com.example.quizwiz.databinding.ActivitySignUpBinding
import com.example.quizwiz.fireBase.FireBaseClass
import com.example.quizwiz.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private var binding: ActivitySignUpBinding? = null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        binding?.tvLoginPage?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener { registerUser() }
    }

    private fun registerUser()
    {
        val name = binding?.etSinUpName?.text.toString()
        val email = binding?.etSinUpEmail?.text.toString()
        val password = binding?.etSinUpPassword?.text.toString()
        if (validateForm(name,email,password))
        {
            val pb = Base.showProgressBar(this)
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    val userId = task.result.user?.uid
                    val userInfo = UserModel(name = name, id = userId!!, emailId = email)
                    FireBaseClass().registerUser(userInfo)
                    Base.showToast(this,"User Id created successfully")
                    pb.cancel()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                else {
                    Base.showToast(this, "User Id not created. Try again later")
                    pb.cancel()
                }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name)->{
                binding?.tilName?.error = "Enter name"
                false
            }
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.tilEmail?.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password)->{
                binding?.tilPassword?.error = "Enter password"
                false
            }
            else -> { true }
        }
    }


}