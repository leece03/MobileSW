package com.example.re0.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.re0.data.Challenge1   // ★ 패키지 경로 확인! (사용자 파일 위치에 맞게 수정)
import com.example.re0.model.DailyRecord // ★ 패키지 경로 확인!
import com.example.re0.model.Profile      // 기존 Profile이 있다면 포함

// [중요] entities 안에 새로 만든 클래스들을 꼭 넣어야 합니다.
@Database(
    entities = [
        Challenge1::class,
        DailyRecord::class,
        Profile::class // Profile도 사용 중이라면 포함
    ],
    version = 2, // ★ 테이블이 변경되었으므로 버전을 1에서 2로 올려주세요
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun achievementDao(): AchievementDao
    abstract fun profileDao(): ProfileDao
}