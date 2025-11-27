package com.example.re0.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.re0.R
import com.example.re0.components.CardTemplate
import com.example.re0.components.NewsCard
import com.example.re0.components.QuizScreen
import com.example.re0.model.NewsItem
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen( navController: NavController, viewModel: QuizViewModel,backStackEntry: NavBackStackEntry) {
    val newsList = listOf(
        NewsItem(
            title = "폐플라스틱 재활용, '화학적 분해' 혁신 기술 도입",
            content = "열분해 기술로 복합 재질 플라스틱을 고품질 원료로 되돌리는 기술이 상용화를 앞두고 있습니다.",
            source = "과학기술정보통신부",
            date = "2025. 10. 28.",
            imgId = R.drawable.rectangle1_1
        ),
        NewsItem(
            title = "AI 기술로 재활용 분류 정확도 향상",
            content = "카메라 인식과 딥러닝을 이용한 자동 분류 시스템이 전국 지자체에 확대될 예정입니다.",
            source = "환경부",
            date = "2025. 11. 01.",
            imgId = R.drawable.rectangle1_2
        ),
        NewsItem(
            title = "폐플라스틱 재활용, '화학적 분해' 혁신 기술 도입",
            content = "열분해 기술로 복합 재질 플라스틱을 고품질 원료로 되돌리는 기술이 상용화를 앞두고 있습니다.",
            source = "과학기술정보통신부",
            date = "2025. 10. 28.",
            imgId = R.drawable.rectangle1_1
        ),NewsItem(
            title = "폐플라스틱 재활용, '화학적 분해' 혁신 기술 도입",
            content = "열분해 기술로 복합 재질 플라스틱을 고품질 원료로 되돌리는 기술이 상용화를 앞두고 있습니다.",
            source = "과학기술정보통신부",
            date = "2025. 10. 28.",
            imgId = R.drawable.rectangle1_1
        ),

        NewsItem(
        title = "폐플라스틱 재활용, '화학적 분해' 혁신 기술 도입",
        content = "열분해 기술로 복합 재질 플라스틱을 고품질 원료로 되돌리는 기술이 상용화를 앞두고 있습니다.",
        source = "과학기술정보통신부",
        date = "2025. 10. 28.",
        imgId = R.drawable.rectangle1_1
    )
    )

    Scaffold (
        topBar = { TopBar() },
        bottomBar ={ BottomBar(navController)}
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            CardTemplate(
                topColor=Mint,
                bottomColor=Color.White,
                borderLineColor = Mint,
                topContent = {
                    Text(text="오늘의 제로웨이스트 TIP", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold )
                },
                bottomContent = {
                    Text(text="비누 끝 조각 합치기 프로젝트", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold )
                    Text("욕실에서 쓰다 남은 비누 조각들 따뜻한 물에 잠깐 녹인 다음  눌러 담으면 새 비누 완성 \uD83E\uDDFC",fontSize = 18.sp, color = Color.Black)
                }
            )

            CardTemplate(
                topColor=White,
                bottomColor=Color.White,
                borderLineColor = Mint,
                topContent = {
                    Text(text="오늘의 다짐", color =Mint,  fontSize = 30.sp, fontWeight = FontWeight.Bold )
                },
                bottomContent = {
                    Text("종이 영수증 받지 않기!", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold )
                }
            )

            CardTemplate(
                topColor=Mint,
                bottomColor=Color.White,
                topContent = {
                    Text(text="OX 퀴즈", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold )
                },
                bottomContent = {
                    QuizScreen(viewModel)
                }
            )

            CardTemplate(
                topColor=White,
                bottomColor=Color.White,
                topContent = {
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal =5.dp)                        ,
                        horizontalArrangement = Arrangement.Start
                    ){
                    Text(text="News", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold )
                        }
                },
                bottomContent = {
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal =5.dp)                        ,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("재활용 관련 뉴스 ", color = Color.Black, fontSize = 25.sp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        newsList.forEach { news ->
                            NewsCard(
                                news = news,
                            )
                        }
                    }
                }
            )
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController() // preview 전용
    HomeScreen(navController = navController)
}


 */