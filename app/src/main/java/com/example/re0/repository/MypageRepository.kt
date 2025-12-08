package com.example.re0.repository

import com.example.re0.data.AchievementDao
import com.example.re0.data.ProfileDao
import com.example.re0.model.DailyRecord
import com.example.re0.model.Profile
import javax.inject.Inject

class MypageRepository @Inject constructor(
    private val achievementDao: AchievementDao,
    private val profileDao: ProfileDao
) {
    suspend fun getRecordsByDate(date: String) = achievementDao.getRecordsByDate(date)
    suspend fun getAllRecords() = achievementDao.getAllRecords()

    // ViewModel에서 호출하는 함수들 연결
    suspend fun addRecord(record: DailyRecord) = achievementDao.insertRecord(record)
    suspend fun updateRecord(record: DailyRecord) = achievementDao.updateRecord(record)
    suspend fun deleteRecord(record: DailyRecord) = achievementDao.deleteRecord(record)

    // 프로필 관련
    suspend fun getProfile() = profileDao.getProfile()
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)
}