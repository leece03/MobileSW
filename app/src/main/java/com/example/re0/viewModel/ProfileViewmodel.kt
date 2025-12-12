package com.example.re0.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.R
import com.example.re0.model.Achievement // ★ 기존 모델 (UI 표시용)
import com.example.re0.model.Profile
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        Profile(
            imageUrl = R.drawable.rectangle1_2,
            name = "",
            email = ""
        )
    )
        private set

    // 리스트에는 '변환된 기록' + '획득한 뱃지'가 모두 들어갑니다.
    var achievementsState by mutableStateOf<List<Achievement>>(emptyList())
        private set

    init {
        loadProfile()
        loadRecords()
    }

    // [수정] 뱃지 개수 계산: 실제 '뱃지(isBadge=true)'인 것만 셉니다.
    fun badgeCount(): Int = achievementsState.count { it.isBadge }

    private fun loadRecords() {
        viewModelScope.launch {
            // 1. 실제 DB 기록(DailyRecord) 가져오기
            val records = repository.getAllRecords()

            // 2. 기록(DailyRecord) -> Achievement 변환 (그래프 및 목록용)
            val historyList = records.map { record ->
                Achievement(
                    id = record.id,
                    // [수정] 중요! 날짜가 아니라 진짜 제목을 넣어야 키워드 검사가 가능합니다.
                    title = record.title,
                    description = null,
                    date = record.date,
                    isDone = record.isDone,
                    // [수정] 아이콘 ID가 0이면 앱이 꺼지므로 기본 이미지(earth)로 대체
                    iconUrl = if (record.iconUrl == 0) R.drawable.earth else record.iconUrl,
                    isBadge = false // 이건 기록이지 뱃지가 아님
                )
            }

            // 3. [신규 기능] 기록을 분석해서 '뱃지' 생성하기
            val earnedBadges = generateBadgesFromRecords(historyList)

            // 4. 두 리스트 합치기 (기록 + 뱃지)
            achievementsState = historyList + earnedBadges

            Log.d("ProfileVM", "Total Loaded: ${achievementsState.size} (Badges: ${earnedBadges.size})")
        }
    }

    // ★ 뱃지 생성 로직 (텀블러, 재활용, 일회용품 키워드 적용)
    private fun generateBadgesFromRecords(records: List<Achievement>): List<Achievement> {
        val badges = mutableListOf<Achievement>()

        // 1. 전체 성공 횟수 확인
        val totalSuccessCount = records.count { it.isDone }

        // 2. 키워드별 성공 횟수 카운트 (제목에 단어가 포함되고, 완료된 것)
        val tumblerCount = records.count { it.isDone && it.title.contains("텀블러") }
        val recycleCount = records.count { it.isDone && it.title.contains("재활용") }
        val disposableCount = records.count { it.isDone && it.title.contains("일회용품") }

        // --- [A. 기본 횟수 뱃지] ---
        if (totalSuccessCount >= 1) {
            badges.add(Achievement(9001, "첫 시작", "첫 번째 목표 달성!", "배지", R.drawable.badge1, true, true))
        }
        if (totalSuccessCount >= 3) {
            badges.add(Achievement(9003, "작심삼일 탈출", "3번 이상 목표 달성", "배지", R.drawable.badge1, true, true))
        }
        if (totalSuccessCount >= 7) {
            badges.add(Achievement(9007, "럭키 세븐", "7번 이상 목표 달성", "배지", R.drawable.badge1, true, true))
        }

        // --- [B. 키워드 뱃지: 텀블러] ---
        if (tumblerCount >= 1) {
            badges.add(Achievement(8001, "텀블러 초보자", "일회용 컵 대신 텀블러 사용!", "환경", R.drawable.badge2, true, true))
        }
        if (tumblerCount >= 10) {
            badges.add(Achievement(8002, "텀블러 마스터", "텀블러 사용이 습관이 되었어요", "환경", R.drawable.badge2, true, true))
        }

        // --- [C. 키워드 뱃지: 재활용] ---
        if (recycleCount >= 1) {
            badges.add(Achievement(8101, "분리수거 왕", "올바른 재활용 실천", "환경", R.drawable.badge2, true, true))
        }

        // --- [D. 키워드 뱃지: 일회용품] ---
        if (disposableCount >= 1) {
            badges.add(Achievement(8201, "제로 웨이스트", "일회용품 사용 줄이기 성공", "환경", R.drawable.badge3, true, true))
        }

        // --- [E. 스페셜 뱃지: 그랜드 슬램] ---
        if (tumblerCount >= 1 && recycleCount >= 1 && disposableCount >= 1) {
            badges.add(Achievement(9999, "지구 지킴이", "3가지 환경 활동 모두 달성!", "스페셜", R.drawable.badge3, true, true))
        }

        return badges
    }

    private fun loadProfile() {
        viewModelScope.launch {
            var loaded = repository.getProfile()
            if (loaded == null) {
                repository.insertProfile(
                    Profile(
                        imageUrl = R.drawable.earth,
                        name = "홍길동",
                        email = "hong@test.com"
                    )
                )
                loaded = repository.getProfile()
            }

            loaded?.let {
                uiState = it
            }

            // 프로필 로드 후 기록도 갱신
            loadRecords()

            Log.d("ProfileVM", "loadProfile() = $loaded")
        }
    }

    fun updateUserProfile(name: String, email: String) {
        val updated = uiState.copy(name = name, email = email)
        viewModelScope.launch {
            repository.updateProfile(updated)
            uiState = updated
        }
    }
}