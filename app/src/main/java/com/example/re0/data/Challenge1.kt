package com.example.re0.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenge1(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String, // 챌린지 이름
    val iconUrl: Int = 0
)