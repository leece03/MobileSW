package com.example.re0.screens

// [1] 모든 Import는 파일 맨 위에 있어야 합니다.
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.re0.model.DailyRecord // ★ 일일 기록 모델
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.AchievementViewModel
// [2] 구형 날짜 라이브러리 (API 레벨 호환성용)
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.filter

// ------------------------------
// [3] 색상 상수 정의
// ------------------------------
val AchievementYellow = Color(0xFFFFE082) // 1/3 달성
val AchievementSkyBlue = Color(0xFF81D4FA) // 2/3 달성
val AchievementMint = Color(0xFF00E676)    // 100% 달성
val AchievementGray = Color(0xFFF5F5F5)    // 기본 배경

// ------------------------------
// [4] 날짜별 색상 계산 함수 (DailyRecord 사용)
// ------------------------------
fun getDayColor(
    date: Date,
    records: List<DailyRecord>
): Color {
    // 날짜를 "yyyy-MM-dd" 문자열로 변환
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
// [5] 커스텀 캘린더 컴포넌트
// ------------------------------
@Composable
fun CustomStatsCalendar(
    records: List<DailyRecord>, // Achievement 대신 DailyRecord 리스트를 받음
    onDateSelected: (String) -> Unit
) {
    // Calendar 인스턴스 사용하여 상태 유지
    var currentCalendar by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // [A] 상단: 년/월 표시 및 이동 버튼
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
            Text(
                text = "${year}년 ${month}월",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = {
                val newCal = currentCalendar.clone() as Calendar
                newCal.add(Calendar.MONTH, 1)
                currentCalendar = newCal
            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // [B] 요일 헤더
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // [C] 날짜 그리드
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
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(40.dp))
            }

            items(daysInMonth) { index ->
                val day = index + 1
                val dayCal = currentCalendar.clone() as Calendar
                dayCal.set(Calendar.DAY_OF_MONTH, day)
                val date = dayCal.time

                // 색상 계산 (DailyRecord 리스트 전달)
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
// [6] 메인 화면
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
// [7] 추가 다이얼로그
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
// [8] 본문 컨텐츠 (리스트 + 달력 연결)
// ------------------------------
@Composable
fun ChallengeContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: AchievementViewModel
) {
    // ★ ViewModel에서 만든 "화면용 리스트(uiList)"와 "달력용 기록(calendarRecords)" 구독
    val uiList by viewModel.uiList.collectAsState()
    val calendarRecords by viewModel.calendarRecords.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newGoal ->
                viewModel.addChallenge(newGoal) // 이름 변경 반영 (addAchievement -> addChallenge)
                showAddDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(29.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        // [섹션 1] 챌린지 등록
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(size = 10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(color = Color(0xFF79ECF4), start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = strokeWidth)
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "챌린지 등록",
                    fontSize = 24.sp,
                    lineHeight = 33.6.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                )
            }

            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "나만의 챌린지를 등록해 보세요",
                    fontSize = 16.sp,
                    color = Color(0xFF656565),
                )
                Column(
                    modifier = Modifier
                        .width(348.dp)
                        .height(42.dp)
                        .background(color = Color(0xFFE3F3FB), shape = RoundedCornerShape(size = 13.125.dp))
                        .clickable { showAddDialog = true }
                        .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "입력하기",
                        fontSize = 16.sp,
                        color = Color(0xFF656565),
                    )
                }
            }
        }

        // [섹션 2] 달성 기록
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(size = 10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(color = Color(0xFF79ECF4), start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = strokeWidth)
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "달성 기록",
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                )
                Text(
                    text = "나만의 목표 등록 및 달성 기록",
                    fontSize = 16.sp,
                    color = Color(0xFF656565),
                )
            }

            Column(
                modifier = Modifier
                    .background(color = Color(0xEBFFFFFF), shape = RoundedCornerShape(size = 15.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                // ★ 커스텀 캘린더 (DailyRecord 리스트 전달)
                CustomStatsCalendar(
                    records = calendarRecords,
                    onDateSelected = { selectedDate ->
                        Log.d("CALENDAR", "$selectedDate 선택됨")
                    }
                )

                Divider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    color = Mint,
                    thickness = 1.dp
                )

                // ★ 리스트 렌더링 (uiList 사용)
                if (uiList.isEmpty()) {
                    Text(
                        text = "등록된 챌린지가 없습니다.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                } else {
                    uiList.forEach { item ->
                        ChallengeGoalItem(
                            text = item.challenge.title, // 챌린지 이름
                            checked = item.isDoneToday,  // 오늘의 성공 여부
                            onCheckedChange = { isChecked ->
                                // 체크 상태 변경 시 (Challenge1 객체를 넘김)
                                viewModel.toggleCheck(item.challenge, isChecked)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(1.4f)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .background(
                    color = Color(0xFFD4F1FD),
                    shape = RoundedCornerShape(13.125.dp)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color(0xFF656565)
            )
        }
    }
}