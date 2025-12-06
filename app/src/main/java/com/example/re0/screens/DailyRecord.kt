package com.example.re0.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index // ★ Import 추가
import androidx.room.PrimaryKey
import com.example.re0.data.Challenge1

@Entity(
    foreignKeys = [ForeignKey(
        entity = Challenge1::class,
        parentColumns = ["id"],
        childColumns = ["challengeId"],
        onDelete = ForeignKey.CASCADE
    )],
    // ★ [추가] 검색 속도 향상 및 경고 제거를 위해 인덱스 추가
    indices = [Index(value = ["challengeId"])]
)
data class DailyRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val challengeId: Int,
    val date: String,
    val isDone: Boolean
)