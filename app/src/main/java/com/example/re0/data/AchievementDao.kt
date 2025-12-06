package com.example.re0.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.re0.data.Challenge1
import com.example.re0.model.DailyRecord


@Dao
interface AchievementDao {
    // [1] 챌린지 목록 (Challenge1)
    @Insert
    suspend fun insertChallenge(challenge: Challenge1)

    @Query("SELECT * FROM Challenge1")
    suspend fun getAllChallenges(): List<Challenge1>

    // [2] 일일 기록 (DailyRecord)
    // 오늘 날짜(date)와 챌린지ID로 기록 조회
    @Query("SELECT * FROM DailyRecord WHERE challengeId = :challengeId AND date = :date LIMIT 1")
    suspend fun getRecord(challengeId: Int, date: String): DailyRecord?

    // 기록 저장/업데이트
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DailyRecord)

    // [3] 달력용 (모든 기록 가져오기)
    @Query("SELECT * FROM DailyRecord")
    suspend fun getAllRecords(): List<DailyRecord>
}