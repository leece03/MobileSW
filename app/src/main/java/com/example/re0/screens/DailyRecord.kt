package com.example.re0.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,    // 목표 이름 (예: "물 마시기")
    val isDone: Boolean,  // 성공 여부
    val date: String,     // 날짜 (yyyy-MM-dd) - 이게 핵심!
    val iconUrl: Int = 0  // 아이콘 (기본값 0)
)