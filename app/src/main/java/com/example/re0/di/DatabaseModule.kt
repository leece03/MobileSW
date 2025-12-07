package com.example.re0.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.re0.data.AchievementDao
import com.example.re0.data.AppDatabase
import com.example.re0.data.ProfileDao
import com.example.re0.model.DailyRecord // ★ Import 확인
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        lateinit var instance: AppDatabase

        instance = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db_v10" // ★ 버전을 10으로 올림 (앱 삭제 후 재설치 필수)
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = instance.achievementDao()

                        // 오늘 날짜 구하기
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                        val initialRecords = listOf(
                            DailyRecord(title = "배달음식 안 시켜먹기", date = today, isDone = false, iconUrl = 0),
                            DailyRecord(title = "텀블러 사용하기", date = today, isDone = false, iconUrl = 0)
                        )

                        try {
                            initialRecords.forEach { dao.insertRecord(it) }
                            Log.d("DB", "Default records inserted!")
                        } catch (e: Exception) {
                            Log.e("DB", "Failed to insert default records", e)
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

        INSTANCE = instance
        return instance
    }

    @Provides
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()

    @Provides
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()
}