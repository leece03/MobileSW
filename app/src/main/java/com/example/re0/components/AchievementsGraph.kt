package com.example.re0.components
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re0.model.Achievement
import com.example.re0.ui.theme.Mint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun groupRecent7Days(achievements: List<Achievement>): Map<LocalDate, Int> {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    val today = LocalDate.now()
    val sevenDaysAgo = today.minusDays(6)

    return achievements
        .map { LocalDate.parse(it.date, formatter) }
        .filter { it in sevenDaysAgo..today }
        .groupingBy { it }
        .eachCount()
        .toSortedMap()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AchievementsGraph(
    achievements: List<Achievement>
) {
    CardTemplate(
        topColor = Color.White,
        bottomColor = Color.White,
        borderLineColor = Mint,
        topContent = {
            Text(text = "통계 그래프", color = Color.Black, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("나만의 목표 등록 및 달성 기록", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
        },
        bottomContent = {

            val dailyDate = groupRecent7Days(achievements)

            if (dailyDate.isEmpty()) {
                Text("아직 기록이 없습니다")
                return@CardTemplate
            }

            val maxValue = dailyDate.values.maxOrNull() ?: 1
            val points = dailyDate.values.toList()
            val dateLabels = dailyDate.keys.map { it.format(DateTimeFormatter.ofPattern("MM.dd")) }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // 라벨 공간 확보
                    .padding(vertical = 20.dp)
                    .padding(horizontal = 10.dp)
                    //.padding(start = 10.dp)// 왼쪽 개수 표기

            ) {
                val leftPadding = 35.dp.toPx()
                val width = size.width-20.dp.toPx()
                val height = size.height - 20.dp.toPx() // 텍스트 영역 제외

                val stepX = if (points.size > 1) {
                    width / (points.size - 1) *0.9f
                } else {
                    width // 한 점이면 중앙 배치
                }

                val normalized = points.map { it.toFloat() / maxValue }
                val yAxisValues = (0..maxValue).toList()

                // 점이 2개 이상일 때만 라인 그리기
                if (points.size > 1) {
                    for (i in 1 until points.size) {
                        val x1 = leftPadding +(i - 1) * stepX
                        val y1 = height - normalized[i - 1] * height
                        val x2 = leftPadding +i * stepX
                        val y2 = height - normalized[i] * height

                        drawLine(
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 4f,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                // 점
                normalized.forEachIndexed { index, value ->
                    val x = leftPadding +index * stepX
                    val y = height - value * height
                    drawCircle(
                        color = Color(0xFF4CAF50),
                        radius = 6f,
                        center = Offset(x, y)
                    )
                }

                // 날짜 라벨
                dateLabels.forEachIndexed { index, value ->
                    val x = leftPadding +index * stepX
                    drawContext.canvas.nativeCanvas.drawText(
                        value,
                        x,
                        size.height, // 패딩 확보로 안 짤림
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 28f
                        }
                    )
                }

                yAxisValues.forEach { value ->
                    val posY = height - (value.toFloat() / maxValue) * height

                    drawContext.canvas.nativeCanvas.drawText(
                        value.toString(),
                        0f, // 왼쪽 고정
                        posY,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textAlign = android.graphics.Paint.Align.LEFT
                            textSize = 28f

                        }
                    )
                }
            }
        }
    )
}