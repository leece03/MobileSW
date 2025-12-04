package com.example.re0.repository

import com.example.re0.data.AchievementDao
import com.example.re0.data.ProfileDao
import com.example.re0.model.Achievement
import com.example.re0.model.Profile
import jakarta.inject.Inject

open class MypageRepository @Inject constructor(
    private val achievementDao: AchievementDao,
    private val profileDao: ProfileDao,
) {
    // 1. 챌린지 추가 (이미 잘 작성하셨습니다!)
    suspend fun insertAchievement(achievement: Achievement) = achievementDao.insertAchievement(achievement)

    // 2. 목록 불러오기
    suspend fun getAllAchievement(): List<Achievement> = achievementDao.getAllAchievement()

    // 3. ★ [수정됨] 체크 상태 업데이트 (Boolean 파라미터 추가)
    suspend fun updateAchievementStatus(id: Int, isDone: Boolean) = achievementDao.updateStatus(id, isDone)

    // --- 프로필 관련 코드는 그대로 유지 ---
    suspend fun getProfile(): Profile? = profileDao.getProfile()
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)
    suspend fun deleteProfile(profile: Profile) = profileDao.deleteProfile(profile)
}