package com.example.re0.components

import android.os.Build
import android.util.Log
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
    val today = LocalDate.now()
    val sevenDaysAgo = today.minusDays(6)

    return achievements
        .filter { !it.isBadge } // ★ [핵심] 배지는 그래프 계산에서 제외 (이게 없으면 앱 꺼짐)
        .mapNotNull { achievement ->
            try {
                // 날짜 포맷이 '-' 인지 '.' 인지 몰라도 둘 다 시도
                val dateStr = achievement.date?.trim() ?: ""
                if (dateStr.contains(".")) {
                    LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                } else {
                    LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                }
            } catch (e: Exception) {
                // 날짜가 아닌 데이터("환경" 등)가 있어도 무시하고 넘어감
                null
            }
        }
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
                Text("최근 7일간의 기록이 없습니다", modifier = Modifier.padding(20.dp))
                return@CardTemplate
            }

            val maxValue = dailyDate.values.maxOrNull() ?: 1
            val points = dailyDate.values.toList()
            val dateLabels = dailyDate.keys.map { it.format(DateTimeFormatter.ofPattern("MM.dd")) }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 20.dp)
                    .padding(horizontal = 10.dp)
            ) {
                val leftPadding = 35.dp.toPx()
                val width = size.width - 20.dp.toPx()
                val height = size.height - 20.dp.toPx()

                val stepX = if (points.size > 1) {
                    width / (points.size - 1) * 0.9f
                } else {
                    width
                }

                val normalized = points.map { it.toFloat() / maxValue }
                val yAxisValues = (0..maxValue).toList()

                if (points.size > 1) {
                    for (i in 1 until points.size) {
                        val x1 = leftPadding + (i - 1) * stepX
                        val y1 = height - normalized[i - 1] * height
                        val x2 = leftPadding + i * stepX
                        val y2 = height - normalized[i] * height

                        drawLine(
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 4f,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                normalized.forEachIndexed { index, value ->
                    val x = leftPadding + index * stepX
                    val y = height - value * height
                    drawCircle(
                        color = Color(0xFF4CAF50),
                        radius = 6f,
                        center = Offset(x, y)
                    )
                }

                dateLabels.forEachIndexed { index, value ->
                    val x = leftPadding + index * stepX
                    drawContext.canvas.nativeCanvas.drawText(
                        value,
                        x,
                        size.height,
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
                        0f,
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