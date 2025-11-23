package com.example.re0.components

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.re0.R
import com.example.re0.model.QuizItem
import com.example.re0.viewModel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel = viewModel()) {

    DailyQuizQueue(
        quizQueue = viewModel.quizQueue,
        onAnswerSelected = { isCorrect ->
            viewModel.processAnswer(isCorrect)
        }
    )
}

@Composable
fun DailyQuizQueue( quizQueue: List<QuizItem>,
                    onAnswerSelected: (Boolean) -> Unit){
    val todayQuiz = quizQueue.first()
    var selected by remember { mutableStateOf<String?>("")}
    var isCorrect by remember  { mutableStateOf<Boolean>(false) }

    Text("플라스틱 병의 라벨(비닐)은 제거하고 버려야 한다.", color = Color.Black, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(10.dp))

    if (selected != "") {
        var answerText=""
        if (selected == "yes" && todayQuiz.correctAnswer){
            answerText="정답입니다"
            isCorrect = true
        }else{
            answerText="오답입니다"
            isCorrect =false
        }

        Text(
            text = answerText,
            color = if (isCorrect) Color.Green else Color.Red,
            fontSize = 18.sp
        )
    }

    Row(modifier = Modifier.padding(10.dp)) {
        FloatingActionButton(
            onClick = {
                if(selected == "")
                    selected = "yes";
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
                if(selected == "")
                    selected = "no";
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

