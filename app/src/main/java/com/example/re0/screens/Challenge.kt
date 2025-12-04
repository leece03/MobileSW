package com.example.re0.screens

import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.re0.ui.theme.Mint
import com.example.re0.viewModel.AchievementViewModel


@Composable
fun ChallengeScreen(
    navController: NavController,
    // 1. ViewModel 주입 (Hilt 사용 시)
    viewModel: AchievementViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        // ViewModel을 Content로 전달
        ChallengeContent(innerPadding, navController, viewModel)
    }
}

// 챌린지 추가를 위한 다이얼로그 (새로 추가됨)
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

@Composable
fun ChallengeContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: AchievementViewModel // ViewModel 파라미터 추가
) {
    // 2. DB 데이터 관찰 (StateFlow Collect)
    // RecordScreen과 동일한 데이터를 바라보게 됩니다.
    val achievements by viewModel.achievementList.collectAsState()

    // 다이얼로그 표시 상태
    var showAddDialog by remember { mutableStateOf(false) }

    // 다이얼로그가 켜져있으면 표시
    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newGoal ->
                // 3. DB에 추가 요청 (ViewModel 함수 호출)
                // ※ ViewModel에 addAchievement 함수가 있다고 가정합니다.
                viewModel.addAchievement(newGoal)
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
        // --- 상단: 챌린지 등록 섹션 ---
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(size = 10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ... (제목 부분 UI 코드는 동일) ...
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
                    lineHeight = 22.4.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF656565),
                )
                // 입력 버튼 영역
                Column(
                    modifier = Modifier
                        .width(348.dp)
                        .height(42.dp)
                        .background(color = Color(0xFFE3F3FB), shape = RoundedCornerShape(size = 13.125.dp))
                        // 클릭 시 다이얼로그 오픈
                        .clickable { showAddDialog = true }
                        .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "입력하기",
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF656565),
                    )
                }
            }
        }

        // --- 하단: 달성 기록 섹션 ---
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(size = 10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ... (제목 부분 UI 코드는 동일) ...
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
                    lineHeight = 33.6.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                )
                Text(
                    text = "나만의 목표 등록 및 달성 기록",
                    fontSize = 16.sp,
                    lineHeight = 22.4.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF656565),
                )
            }

            Column(
                modifier = Modifier
                    .background(color = Color(0xEBFFFFFF), shape = RoundedCornerShape(size = 15.dp))
                    .padding(start = 10.dp, top = 14.dp, end = 10.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                DefaultCalendarView { y, m, d ->
                    Log.d("CALENDAR", "$y-$m-$d 선택됨")
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Mint,
                    thickness = 1.dp
                )

                // 4. 리스트 렌더링 (DB 데이터 기반)
                if (achievements.isEmpty()) {
                    // 데이터가 없을 때 표시할 텍스트 (옵션)
                    Text(
                        text = "등록된 챌린지가 없습니다.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                } else {
                    achievements.forEach { item ->
                        ChallengeGoalItem(
                            text = item.title,
                            checked = item.isDone,
                            onCheckedChange = { isChecked ->
                                // 5. 체크 상태 변경 시 DB 업데이트
                                viewModel.markDone(item.id, isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ... (ChallengeGoalItem, DefaultCalendarView 등의 컴포넌트는 기존 코드 유지)
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
            .padding(start = 2.dp, top = 6.dp, end = 7.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 체크박스
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(1.4f)
        )

        // 회색 영역
        Row(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .background(
                    color = Color(0xFFD4F1FD),
                    shape = RoundedCornerShape(13.125.dp)
                )
                .padding(start = 18.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                lineHeight = 22.4.sp,
                fontWeight = FontWeight.W400,
                color = Color(0xFF656565)
            )
        }
    }
}

@Composable
fun DefaultCalendarView(
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onDateSelected(year, month + 1, dayOfMonth)
                }
            }
        }
    )
}