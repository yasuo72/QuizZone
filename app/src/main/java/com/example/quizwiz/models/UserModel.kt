package com.example.quizwiz.models

data class UserModel(
    val id:String = "",
    val emailId:String = "",
    val name:String = "",
    val image:String = "",
    val allTimeScore:Double = 0.0,
    val weeklyScore:Double = 0.0,
    val monthlyScore:Double = 0.0,
    val lastGameScore:Double = 0.0
)
