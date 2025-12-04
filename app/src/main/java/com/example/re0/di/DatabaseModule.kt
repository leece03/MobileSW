package com.example.re0.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.re0.data.AchievementDao
import com.example.re0.data.AppDatabase
import com.example.re0.data.ProfileDao
import com.example.re0.data.Challenge1 // ★ 모델 import 확인
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            "app_db_v5" // ★ 버전 올려서 초기화 (혹은 앱 삭제 후 재실행)
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = instance.achievementDao()

                        // ★ [수정] Challenge1에 맞는 초기 데이터
                        val initialChallenges = listOf(
                            Challenge1(title = "배달음식 안 시켜먹기"),
                            Challenge1(title = "텀블러 사용하기"),
                            Challenge1(title = "분리수거 잘하기")
                        )

                        try {
                            // ★ [수정] insertAll 대신 하나씩 추가
                            initialChallenges.forEach {
                                dao.insertChallenge(it)
                            }
                            Log.d("DB", "Default challenges inserted!")
                        } catch (e: Exception) {
                            Log.e("DB", "Failed to insert default challenges", e)
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

        INSTANCE = instance
        Log.d("DB", "Database instance created")

        return instance
    }

    @Provides
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()

    @Provides
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()
}