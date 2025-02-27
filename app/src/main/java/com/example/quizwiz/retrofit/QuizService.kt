package com.example.quizwiz.retrofit

import com.example.quizwiz.models.QuestionStats
import com.example.quizwiz.models.QuizResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizService {
    @GET("api.php")
    fun getQuiz(
        @Query("amount") amount:Int,
        @Query("category")category:Int?,
        @Query("difficulty")difficulty:String?,
        @Query("type") type:String?
    ):Call<QuizResponse>
}

interface QuestionStatsService{
    @GET("api_count_global.php")
    fun getData():Call<QuestionStats>
}