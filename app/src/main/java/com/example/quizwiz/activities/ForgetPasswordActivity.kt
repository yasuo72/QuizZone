package com.example.quizwiz.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizwiz.constants.Base
import com.example.quizwiz.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity: AppCompatActivity() {
    private var binding: ActivityForgetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        binding?.btnForgotPasswordSubmit?.setOnClickListener { resetPassword() }
    }

    private fun validateForm(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.tilEmailForgetPassword?.error = "Enter valid email address"
                false
            }
            else -> true
        }

    }

    private fun resetPassword()
    {
        val email = binding?.etForgotPasswordEmail?.text.toString()
        if (validateForm(email))
        {
            val pb = Base.showProgressBar(this)
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    pb.cancel()
                    binding?.tilEmailForgetPassword?.visibility = View.GONE
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                }
                else
                {
                    pb.cancel()
                    Base.showToast(this,"Can not reset your password. Try after sometime")
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}