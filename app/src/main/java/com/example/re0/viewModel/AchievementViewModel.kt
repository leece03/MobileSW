package com.example.re0.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.model.DailyRecord
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat // ★ 구형 API 사용 (오류 해결)
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    // 화면용 리스트 (오늘 것만)
    private val _uiList = MutableStateFlow<List<DailyRecord>>(emptyList())
    val uiList: StateFlow<List<DailyRecord>> = _uiList

    // 달력용 전체 기록
    private val _calendarRecords = MutableStateFlow<List<DailyRecord>>(emptyList())
    val calendarRecords: StateFlow<List<DailyRecord>> = _calendarRecords

    init {
        loadData()
    }

    // [API 24 호환] 오늘 날짜 구하기
    private fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun loadData() {
        viewModelScope.launch {
            val today = getTodayDate()
            // 1. 오늘 날짜 기록만 가져옴 (내일 되면 리스트 비워짐)
            _uiList.value = repository.getRecordsByDate(today)
            // 2. 달력용 전체 기록
            _calendarRecords.value = repository.getAllRecords()
        }
    }

    // 추가 (오늘 날짜로 저장)
    fun addChallenge(title: String) {
        viewModelScope.launch {
            val newRecord = DailyRecord(
                title = title,
                isDone = false,
                date = getTodayDate(), // 오늘 날짜 박제
                iconUrl = 0 // [오류 해결] 기본값 추가
            )
            repository.addRecord(newRecord)
            loadData()
        }
    }

    // 체크 (성공 여부 변경)
    fun toggleCheck(record: DailyRecord, isChecked: Boolean) {
        viewModelScope.launch {
            val updated = record.copy(isDone = isChecked)
            repository.updateRecord(updated)
            loadData()
        }
    }

    // 수정 (제목 변경)
    fun updateChallenge(record: DailyRecord, newTitle: String) {
        viewModelScope.launch {
            val updated = record.copy(title = newTitle)
            repository.updateRecord(updated)
            loadData()
        }
    }

    // 삭제
    fun deleteChallenge(record: DailyRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            loadData()
        }
    }
}