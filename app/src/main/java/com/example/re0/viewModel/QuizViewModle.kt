package com.example.re0.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.re0.model.QuizItem

class QuizViewModel : ViewModel() {
    private val _quizQueue = mutableStateListOf(
        QuizItem(0, "플라스틱 병의 라벨은 제거해야 한다.", true),
        QuizItem(1, "유리병은 일반쓰레기로 버린다.", false),
        QuizItem(2, "음식물 쓰레기는 종량제 봉투에 넣는다.", false),
        QuizItem(3, "캔은 압착하지 않고 그대로 버린다.", false),
    )
    val quizQueue: SnapshotStateList<QuizItem> = _quizQueue

    fun processAnswer(isCorrect: Boolean) {
        val todayQuiz = _quizQueue.first()
        _quizQueue.removeAt(0)

        if (isCorrect) {
            _quizQueue.add(todayQuiz)
        } else {
            val mid = _quizQueue.size / 2
            _quizQueue.add(mid, todayQuiz)
        }
    }
}
