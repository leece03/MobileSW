package com.example.re0.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.re0.model.DailyRecord

@Dao
interface AchievementDao {
    // 1. 오늘 날짜의 할 일만 가져오기 (리스트용)
    @Query("SELECT * FROM DailyRecord WHERE date = :date")
    suspend fun getRecordsByDate(date: String): List<DailyRecord>

    // 2. 전체 기록 가져오기 (달력 색칠용)
    @Query("SELECT * FROM DailyRecord")
    suspend fun getAllRecords(): List<DailyRecord>

    // 3. 추가, 수정, 삭제 기능
    @Insert
    suspend fun insertRecord(record: DailyRecord)

    @Update
    suspend fun updateRecord(record: DailyRecord)

    @Delete
    suspend fun deleteRecord(record: DailyRecord)
}