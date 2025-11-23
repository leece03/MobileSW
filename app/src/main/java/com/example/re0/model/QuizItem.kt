package com.example.re0.model

import androidx.room.PrimaryKey

data class QuizItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val question: String,
    val correctAnswer: Boolean
)