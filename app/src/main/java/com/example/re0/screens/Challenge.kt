package com.example.re0.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.re0.model.DailyRecord
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.AchievementViewModel
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

// ------------------------------
// [1] 색상 상수
// ------------------------------
val AchievementYellow = Color(0xFFFFE082) // 1/3 달성
val AchievementSkyBlue = Color(0xFF81D4FA) // 2/3 달성
val AchievementMint = Mint    // 100% 달성
val AchievementGray = Color(0xFFF5F5F5)    // 기본 배경

// ------------------------------
// [2] 날짜별 색상 계산 함수 (수정됨: DailyRecord 사용)
// ------------------------------
fun getDayColor(
    date: Date,
    records: List<DailyRecord> // ★ [수정] 입력 타입을 DailyRecord 리스트로 변경
): Color {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateString = dateFormat.format(date)

    // 해당 날짜의 기록 필터링
    val dailyRecords = records.filter { it.date == dateString }

    val total = dailyRecords.size
    if (total == 0) return Color.Transparent

    val doneCount = dailyRecords.count { it.isDone }
    val ratio = doneCount.toFloat() / total.toFloat()

    return when {
        ratio == 1.0f -> AchievementMint
        ratio >= 0.66f -> AchievementSkyBlue
        ratio >= 0.33f -> AchievementYellow
        else -> AchievementGray
    }
}

// ------------------------------
// [3] 커스텀 캘린더 컴포넌트 (수정됨: DailyRecord 사용)
// ------------------------------
@Composable
fun CustomStatsCalendar(
    records: List<DailyRecord>, // ★ [수정] 입력 타입을 DailyRecord 리스트로 변경
    onDateSelected: (String) -> Unit
) {
    var currentCalendar by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // [상단] 년/월 표시 및 이동 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val newCal = currentCalendar.clone() as Calendar
                newCal.add(Calendar.MONTH, -1)
                currentCalendar = newCal
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Prev")
            }

            val year = currentCalendar.get(Calendar.YEAR)
            val month = currentCalendar.get(Calendar.MONTH) + 1
            Text(text = "${year}년 ${month}월", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = {
                val newCal = currentCalendar.clone() as Calendar
                newCal.add(Calendar.MONTH, 1)
                currentCalendar = newCal
            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // [중단] 요일 헤더
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            daysOfWeek.forEach { day ->
                Text(text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // [하단] 날짜 그리드
        val daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val tempCal = currentCalendar.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(260.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(firstDayOfWeek) { Box(modifier = Modifier.size(40.dp)) }

            items(daysInMonth) { index ->
                val day = index + 1
                val dayCal = currentCalendar.clone() as Calendar
                dayCal.set(Calendar.DAY_OF_MONTH, day)
                val date = dayCal.time

                // ★ [수정] records(DailyRecord 리스트)를 넘겨줍니다.
                val backgroundColor = getDayColor(date, records)
                val isTransparent = backgroundColor == Color.Transparent

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (isTransparent) Color.Transparent else backgroundColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            onDateSelected(sdf.format(date))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (!isTransparent && backgroundColor != AchievementYellow) Color.White else Color.Black,
                        fontWeight = if (!isTransparent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// ------------------------------
// [4] 메인 화면
// ------------------------------
@Composable
fun ChallengeScreen(
    navController: NavController,
    viewModel: AchievementViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        ChallengeContent(innerPadding, navController, viewModel)
    }
}

// ------------------------------
// [5] 추가 다이얼로그
// ------------------------------
@Composable
fun AddChallengeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "새 챌린지 추가") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("목표를 입력하세요") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                if (text.isNotBlank()) {
                    onConfirm(text)
                }
            }) {
                Text("등록")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

// ------------------------------
// [6] 본문 컨텐츠
// ------------------------------
@Composable
fun ChallengeContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: AchievementViewModel
) {
    val uiList by viewModel.uiList.collectAsState()
    val calendarRecords by viewModel.calendarRecords.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newGoal ->
                viewModel.addChallenge(newGoal)
                showAddDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(29.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        // [챌린지 등록 섹션]
        Column(
            modifier = Modifier.border(1.dp, Mint, RoundedCornerShape(10.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth().drawBehind { drawLine(Color(0xFF79ECF4), Offset(0f, size.height), Offset(size.width, size.height), 5f) }.padding(5.dp)) {
                Text("챌린지 등록", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("나만의 챌린지를 등록해 보세요", fontSize = 16.sp, color = Color.Gray)
                Box(modifier = Modifier.width(348.dp).height(42.dp).background(Color(0xFFE3F3FB), RoundedCornerShape(13.dp)).clickable { showAddDialog = true }.padding(start = 18.dp), contentAlignment = Alignment.CenterStart) {
                    Text("입력하기", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }

        // [달성 기록 섹션]
        Column(
            modifier = Modifier.border(1.dp, Mint, RoundedCornerShape(10.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth().drawBehind { drawLine(Color(0xFF79ECF4), Offset(0f, size.height), Offset(size.width, size.height), 5f) }.padding(5.dp)) {
                Text("달성 기록", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("나만의 목표 등록 및 달성 기록", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Column(
                modifier = Modifier.background(Color(0xEBFFFFFF), RoundedCornerShape(15.dp)).padding(10.dp)
            ) {
                // ★ 커스텀 캘린더 (이제 에러가 나지 않습니다!)
                CustomStatsCalendar(
                    records = calendarRecords,
                    onDateSelected = { Log.d("CALENDAR", "$it") }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Mint, thickness = 1.dp)

                // 리스트 렌더링
                if (uiList.isEmpty()) {
                    Text("등록된 챌린지가 없습니다.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                } else {
                    uiList.forEach { item ->
                        ChallengeGoalItem(
                            text = item.title,
                            checked = item.isDone,
                            onCheckedChange = { isChecked ->
                                viewModel.toggleCheck(item, isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeGoalItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().height(54.dp).padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.scale(1.4f))
        Row(modifier = Modifier.weight(1f).height(42.dp).background(Color(0xFFD4F1FD), RoundedCornerShape(13.dp)).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, fontSize = 16.sp, color = Color.Gray)
        }
    }
}