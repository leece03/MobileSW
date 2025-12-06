package com.example.re0.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.R
import com.example.re0.model.Achievement // ★ 기존 모델 Import (Mypage 호환용)
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

    // ★ [복구] Mypage.kt가 이 이름을 찾고 있어서 다시 추가했습니다.
    // DailyRecord 데이터를 Achievement 형식으로 변환해서 보여줍니다.
    var achievementsState by mutableStateOf<List<Achievement>>(emptyList())
        private set

    init {
        loadProfile()
        loadRecords()
    }

    // 뱃지 개수 계산 (완료된 기록 수)
    fun badgeCount(): Int = achievementsState.count { it.isDone }

    private fun loadRecords() {
        viewModelScope.launch {
            // 1. 실제 DB 기록(DailyRecord)을 가져옴
            val records = repository.getAllRecords()

            // 2. Mypage 화면이 깨지지 않도록 Achievement 객체로 변환 (Mapping)
            // (참고: DailyRecord에는 제목이 없으므로 임시로 빈 문자열이나 날짜를 넣습니다)
            achievementsState = records.map { record ->
                Achievement(
                    id = record.id,
                    title = record.date, // 제목 대신 날짜 표시 (임시)
                    isDone = record.isDone,
                    date = record.date,
                    iconUrl = 0,
                    isBadge = false
                )
            }

            Log.d("ProfileVM", "Records loaded and mapped: ${achievementsState.size}")
        }
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