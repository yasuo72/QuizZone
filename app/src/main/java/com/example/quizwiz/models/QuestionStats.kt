package com.example.quizwiz.models

data class QuestionStats(
    val overall: OverallStats,
    val categories: Map<String, CategoryStats>
)

data class OverallStats(
    val total_num_of_questions: Int,
    val total_num_of_pending_questions: Int,
    val total_num_of_verified_questions: Int,
    val total_num_of_rejected_questions: Int
)

data class CategoryStats(
    val total_num_of_questions: Int,
    val total_num_of_pending_questions: Int,
    val total_num_of_verified_questions: Int,
    val total_num_of_rejected_questions: Int
)

