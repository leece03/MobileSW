package com.example.re0.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.data.Challenge1
import com.example.re0.model.DailyRecord
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.map

data class ChallengeUiItem(
    val challenge: Challenge1,
    val isDoneToday: Boolean
)

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    private val _uiList = MutableStateFlow<List<ChallengeUiItem>>(emptyList())
    val uiList: StateFlow<List<ChallengeUiItem>> = _uiList

    private val _calendarRecords = MutableStateFlow<List<DailyRecord>>(emptyList())
    val calendarRecords: StateFlow<List<DailyRecord>> = _calendarRecords

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(Date())

            // Repository 함수 호출 (이제 빨간 줄 안 뜸)
            val challenges = repository.getChallenges()

            val uiItems = challenges.map { challenge ->
                val record = repository.getDailyRecord(challenge.id, today)
                val isDone = record?.isDone ?: false
                ChallengeUiItem(challenge, isDone)
            }
            _uiList.value = uiItems
            _calendarRecords.value = repository.getAllRecords()
        }
    }

    fun addChallenge(title: String) {
        viewModelScope.launch {
            repository.addChallenge(title)
            loadData()
        }
    }

    fun toggleCheck(challenge: Challenge1, isChecked: Boolean) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(Date())

            val oldRecord = repository.getDailyRecord(challenge.id, today)

            // DailyRecord 생성 시 필요한 값만 넣음
            val newRecord = DailyRecord(
                id = oldRecord?.id ?: 0,
                challengeId = challenge.id,
                date = today,
                isDone = isChecked
            )

            repository.saveRecord(newRecord)
            loadData()
        }
    }
}