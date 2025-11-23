package com.example.re0.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.re0.R
import com.example.re0.model.QuizItem
import com.example.re0.viewModel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizScreen(viewModel: QuizViewModel = hiltViewModel()) {

    DailyQuizQueue(
        quizQueue = viewModel.quizQueue,
        selected = viewModel.selected,
        onAnswerSelected = { isCorrect ->
            viewModel.onAnswerSelected(isCorrect)
        }
    )
}
@Composable
fun DailyQuizQueue(  quizQueue: List<QuizItem>,
                     selected: Boolean,
                     onAnswerSelected: (Boolean) -> Unit){
    val todayQuiz = quizQueue.first()

    Text(todayQuiz.question)
    Spacer(Modifier.height(10.dp))

    if (selected) {
        val correctText = if (todayQuiz.correctAnswer) "정답입니다" else "오답입니다"
        Text(correctText, color = Color.Blue, fontSize = 20.sp)
    }

    Row(modifier = Modifier.padding(10.dp)) {
        FloatingActionButton(
            onClick = {
                if( !selected)
                    onAnswerSelected(true)
            },
            modifier = Modifier.width(150.dp),
            containerColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.yes),
                contentDescription = "yes 버튼",
                Modifier.size(50.dp)
            )
        }
        FloatingActionButton(
            onClick = {
                if( !selected)
                    onAnswerSelected(true)
            },
            modifier = Modifier.width(150.dp),
            containerColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.no),
                contentDescription = "no 버튼",
                Modifier.size(50.dp)
            )
        }
    }
}

