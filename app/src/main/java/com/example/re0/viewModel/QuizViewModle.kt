package com.example.re0.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.model.QuizItem
import com.example.re0.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val quizList = listOf(
        QuizItem(0, "플라스틱 병의 라벨은 제거해야 한다.", true),
        QuizItem(1, "유리병은 일반쓰레기로 버린다.", false),
        QuizItem(2, "음식물 쓰레기는 종량제 봉투에 넣는다.", false),
        QuizItem(3, "캔은 압착하지 않고 그대로 버린다.", false),
    )

    var todayIndex by mutableStateOf(0)
        private set

    var selected by mutableStateOf(false)
        private set

    private val _isCorrect = MutableStateFlow<Boolean>(true)
    var isCorrect: StateFlow<Boolean> = _isCorrect.asStateFlow()

    init {
        observeSavedState()
    }

    private fun observeSavedState() {
        viewModelScope.launch {
            combine(
                repository.todayIndex,
                repository.selectedState,
                repository.lastDate
            ) { savedIndex, savedSelected, savedDate ->
                Triple(savedIndex, savedSelected, savedDate)
            }.collect { (savedIndex, savedSelected, savedDate) ->

                val today = LocalDate.now().toString()

                if (savedDate != today) {
                    // 오늘 처음 시작
                    val nextIndex = (savedIndex + 1) % quizList.size
                    todayIndex = nextIndex
                    selected = false

                    repository.updateTodayIndex(nextIndex)
                    repository.updateSelectedState(false)
                    repository.updateLastDate(today)

                } else {
                    // 오늘 이미 시작함 → 복원
                    todayIndex = savedIndex
                    selected = savedSelected
                }
            }
        }
    }

    fun onAnswerSelected(answer: Boolean) {
        if (selected) return  // 이미 오늘 문제 풀었으면 무시

        selected = true

        // 오늘 문제 정답 체크 (필요시 Repository에 저장 가능)
        val todayQuiz = quizList[todayIndex]
        val correct = todayQuiz.correctAnswer == answer

        viewModelScope.launch {
            _isCorrect.value = correct   //이렇게 갱신해야 컴포저블에서 관찰 가능
            repository.updateSelectedState(true)
        }


        viewModelScope.launch {
            repository.updateSelectedState(true)
        }
    }

    fun getTodayQuiz(): QuizItem = quizList[todayIndex]
}