package com.example.re0.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.re0.model.Achievement
import com.example.re0.model.Profile

@Database(
    entities = [Achievement::class, Profile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao
    abstract fun profileDao(): ProfileDao
}