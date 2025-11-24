package com.example.re0.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.re0.R
import com.example.re0.model.QuizItem
import com.example.re0.viewModel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel()
) {
    val selected = viewModel.selected
    val todayQuiz = viewModel.getTodayQuiz()   // todayIndex 기반

    DailyQuizContent(
        quiz = todayQuiz,
        selected = selected,
        onAnswerSelected = { answer ->
            viewModel.onAnswerSelected(answer)
        }
    )
}

@Composable
fun DailyQuizContent(
    quiz: QuizItem,
    selected: Boolean,
    onAnswerSelected: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Text(
            quiz.question,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )


        if (selected) {
            val feedback = if (quiz.correctAnswer) "정답입니다!" else "오답입니다!"
            Text(
                feedback,
                color = Color.Blue,
                fontSize = 20.sp
            )

        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            FloatingActionButton(
                onClick = { if (!selected) onAnswerSelected(true) },
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier.size(120.dp)
                ,
            ) {
                Image(
                    painter = painterResource(R.drawable.yes),
                    contentDescription = "yes 버튼",
                    Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.size(20.dp))
            FloatingActionButton(
                onClick = { if (!selected) onAnswerSelected(false) },
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier.size(120.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.no),
                    contentDescription = "no 버튼",
                    Modifier.size(50.dp)
                )
            }
        }
    }
}
