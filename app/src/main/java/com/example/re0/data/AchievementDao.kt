package com.example.re0.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.re0.model.Achievement

@Dao
interface AchievementDao {
    @Query("SELECT * FROM Achievement")
    suspend fun getAllAchievement(): List<Achievement>

    // ★ [수정 중요] id와 함께 체크 상태(isDone)도 받아야 합니다.
    @Query("UPDATE Achievement SET isDone = :isDone WHERE id = :id")
    suspend fun updateStatus(id: Int, isDone: Boolean)

    @Insert
    suspend fun insertAchievement(achievement: Achievement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Achievement>)
}