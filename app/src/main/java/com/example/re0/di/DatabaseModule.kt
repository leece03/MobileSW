package com.example.re0.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.re0.R
import com.example.re0.data.AchievementDao
import com.example.re0.data.AppDatabase
import com.example.re0.data.ProfileDao
import com.example.re0.model.Achievement
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
        // 1) 인스턴스 변수를 미리 선언해서 콜백이 접근할 수 있게 함
        lateinit var instance: AppDatabase

        instance = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db_v2"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // 2) 이곳에서 "새 DB 생성" 하지 말고 위에서 만든 instance 사용
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = instance.achievementDao()

                        val achievements = listOf(
                                Achievement(
                                    title = "재활용마스터",
                                    iconUrl = R.drawable.badge1,
                                    date = "2025.11.24",
                                    isDone = false,
                                    isBadge=true
                                ),
                                Achievement(
                                    title = "분리수거천재",
                                    iconUrl = R.drawable.badge2,
                                    date = "2025.11.25",
                                    isDone = false,
                                    isBadge=true
                                ),
                                Achievement(
                                    title = "텀블러킹",
                                    iconUrl = R.drawable.badge3,
                                    date = "2025.11.24",
                                    isDone = false,
                                    isBadge=true
                                ),
                                Achievement(
                                    title = "분리수거천재",
                                    iconUrl = R.drawable.badge2,
                                    date = "2025.11.24",
                                    isDone = false,
                                    isBadge=true
                                ),
                                Achievement(
                                    title = "텀블러킹",
                                    iconUrl = R.drawable.badge1,
                                    date = "2025.11.29",
                                    isDone = false,
                                    isBadge=true
                                ),
                                Achievement(
                                    title = "재활용마스터",
                                    iconUrl = R.drawable.badge3,
                                    date = "2025.11.25",
                                    isDone = false
                                ),
                                Achievement(
                                    title = "재활용마스터",
                                    iconUrl = R.drawable.badge2,
                                    date = "2025.11.25",
                                    isDone = false
                                ),
                                Achievement(
                                    title = "분리수거천재",
                                    iconUrl = R.drawable.badge1,
                                    date = "2025.11.28",
                                    isDone = false
                                ),
                                Achievement(
                                    title = "분리수거천재",
                                    iconUrl = R.drawable.badge2,
                                    date = "2025.11.26",
                                    isDone = false
                                ),
                                Achievement(
                                    title = "분리수거천재",
                                    iconUrl = R.drawable.badge1,
                                    date = "2025.11.26",
                                    isDone = false
                                )
                        )
                            try {
                                dao.insertAll(achievements)
                                Log.d("DB", "Default achievements inserted!")
                            } catch (e: Exception) {
                                Log.e("DB", "Failed to insert default achievements", e)
                            }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

        // (선택) 모듈 레벨 INSTANCE 변수에도 할당해두면 다른 곳에서도 재사용 가능
        INSTANCE = instance

        Log.d("DB", "Database instance created")

        return instance
    }

    @Provides
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()

    @Provides
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()
}

