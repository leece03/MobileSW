package com.example.re0.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.model.Achievement
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    private val _achievementList = MutableStateFlow<List<Achievement>>(emptyList())
    val achievementList: StateFlow<List<Achievement>> = _achievementList

    init {
        loadAchievements()
    }

    fun loadAchievements() {
        viewModelScope.launch {
            _achievementList.value = repository.getAllAchievement()
        }
    }

    // ★ [추가] ChallengeScreen에서 이 함수를 호출하면 DB에 저장됩니다.
    fun addAchievement(title: String) {
        viewModelScope.launch {
            // id를 0으로 설정하면 Room이 자동으로 번호(Primary Key)를 생성해줍니다.
            val newAchievement = Achievement(id = 0, title = title, isDone = false)

            // 1. DB에 진짜로 저장
            repository.insertAchievement(newAchievement)

            // 2. 저장 후 목록 새로고침 (화면에 즉시 반영)
            loadAchievements()
        }
    }

    // ★ [수정] 체크 상태(checked)를 받아서 DB에 업데이트
    fun markDone(id: Int, checked: Boolean) {
        viewModelScope.launch {
            repository.updateAchievementStatus(id, checked)
            loadAchievements()
        }
    }
}