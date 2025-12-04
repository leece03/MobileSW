package com.example.re0.repository

import com.example.re0.data.AchievementDao
import com.example.re0.data.Challenge1
import com.example.re0.data.ProfileDao
import com.example.re0.model.Profile
import com.example.re0.model.DailyRecord
import javax.inject.Inject

class MypageRepository @Inject constructor(
    private val achievementDao: AchievementDao,
    private val profileDao: ProfileDao
) {
    // ------------------------------------------------
    // ★ ViewModel에서 호출하는 함수들 구현 (오류 해결)
    // ------------------------------------------------

    // 1. 모든 챌린지 가져오기
    suspend fun getChallenges(): List<Challenge1> {
        return achievementDao.getAllChallenges()
    }

    // 2. 특정 날짜의 기록 가져오기
    suspend fun getDailyRecord(challengeId: Int, date: String): DailyRecord? {
        return achievementDao.getRecord(challengeId, date)
    }

    // 3. 챌린지 추가하기
    suspend fun addChallenge(title: String) {
        // Challenge1 생성 시 iconUrl은 기본값(0)이 들어가므로 title만 넣으면 됨
        achievementDao.insertChallenge(Challenge1(title = title))
    }

    // 4. 기록 저장하기 (체크박스)
    suspend fun saveRecord(record: DailyRecord) {
        achievementDao.insertRecord(record)
    }

    // 5. 달력용 전체 기록 가져오기
    suspend fun getAllRecords(): List<DailyRecord> {
        return achievementDao.getAllRecords()
    }

    // ------------------------------------------------
    // 기존 프로필 관련 코드 (유지)
    // ------------------------------------------------
    suspend fun getProfile(): Profile? = profileDao.getProfile()
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)
    suspend fun deleteProfile(profile: Profile) = profileDao.deleteProfile(profile)
}