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

    @Query("UPDATE Achievement SET isDone = 1 WHERE id = :id")
    suspend fun markDone(id: Int)
    @Insert
    suspend fun insertAchievement(achievement: Achievement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Achievement>)
}