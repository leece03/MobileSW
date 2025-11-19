package com.example.re0.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.re0.model.Achievement
@Dao
interface AchievementDao {
    @Query("SELECT * FROM Achievement")
    fun getAllAchievement(): List<Achievement>

    @Insert
    suspend fun insertAchievement(achievement: Achievement)
}