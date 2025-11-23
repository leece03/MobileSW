package com.example.re0.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.model.QuizItem
import com.example.re0.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _quizQueue = mutableStateListOf(
        QuizItem(0, "플라스틱 병의 라벨은 제거해야 한다.", true),
        QuizItem(1, "유리병은 일반쓰레기로 버린다.", false),
        QuizItem(2, "음식물 쓰레기는 종량제 봉투에 넣는다.", false),
        QuizItem(3, "캔은 압착하지 않고 그대로 버린다.", false),
    )
    val quizQueue: SnapshotStateList<QuizItem> = _quizQueue

    var selected by mutableStateOf(false)
        private set

    init {
        observeSavedState()
        observeDayChange()
    }

    private fun observeSavedState() {
        viewModelScope.launch {
            repository.selectedState.collect {
                selected = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDayChange() {
        viewModelScope.launch {
            repository.lastDate.collect { savedDate ->
                val today = LocalDate.now().toString()

                if (savedDate != today) {
                    moveToNextQuiz()
                    repository.updateLastDate(today)
                    repository.updateSelectedState(false)
                    selected = false
                }
            }
        }
    }

    fun onAnswerSelected(isCorrect: Boolean) {
        selected = true
        viewModelScope.launch {
            repository.updateSelectedState(true)
        }
        processAnswer(isCorrect)
    }

    private fun moveToNextQuiz() {
        val todayQuiz = _quizQueue.removeAt(0)
        _quizQueue.add(todayQuiz)
    }

    private fun processAnswer(isCorrect: Boolean) {
        val todayQuiz = _quizQueue.removeAt(0)

        if (isCorrect) {
            _quizQueue.add(todayQuiz)
        } else {
            val mid = _quizQueue.size / 2
            _quizQueue.add(mid, todayQuiz)
        }
    }
}
