package com.example.re0.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.re0.model.DailyRecord // ★ model 패키지 확인
import com.example.re0.model.Profile

@Database(
    entities = [DailyRecord::class, Profile::class],
    version = 6, // ★ 버전을 올려서 충돌 방지
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun achievementDao(): AchievementDao
    abstract fun profileDao(): ProfileDao
}