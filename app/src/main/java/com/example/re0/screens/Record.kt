package com.example.re0.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.re0.ui.theme.Mint
import com.example.re0.ui.theme.NeonBlue
import com.example.re0.viewModel.AchievementViewModel

@Composable
fun RecordScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val viewModel: AchievementViewModel = hiltViewModel()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        RecordContent(innerPadding, viewModel, navController)
    }
}

@Composable
fun GarbageDayItem(
    day: String,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EditDescriptionDialog(
            initialText = description,
            onConfirm = {
                onDescriptionChange(it)
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .height(130.dp)
            .width(43.dp)
            .background(color = Color(0x2601E7C5), shape = RoundedCornerShape(5.dp))
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = day,
            fontSize = 17.44.sp,
            fontWeight = FontWeight.W400,
            color = Color.Black
        )

        Column(
            modifier = Modifier
                .width(38.88.dp)
                .padding(vertical = 14.dp)
                .clickable { showDialog = true },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = description,
                fontSize = 11.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RecordGoalItem(
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
                    color = Color(0xFFE1E7E9),
                    shape = RoundedCornerShape(13.125.dp)
                )
                .padding(start = 18.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = Color(0xFF656565),
            )
        }
    }
}

@Composable
fun EditDescriptionDialog(
    initialText: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("설명 수정") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(text) }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("취소")
            }
        }
    )
}

@Composable
fun RecordContent(
    innerPadding: PaddingValues,
    viewModel: AchievementViewModel,
    navController: NavController
) {
    // ★ [수정] achievementList 대신 uiList(챌린지+오늘체크여부)를 사용
    val uiList by viewModel.uiList.collectAsState()

    val totalGoals = uiList.size
    val totalChecked = uiList.count { it.isDoneToday }

    val progressPercent = if (totalGoals == 0) 0
    else ((totalChecked.toFloat() / totalGoals.toFloat()) * 100).toInt()

    val progressFraction = progressPercent / 100f

    // ----------------------------

    var monday by remember { mutableStateOf("재활용 수거일") }
    var tuesday by remember { mutableStateOf("일반 쓰레기") }
    var wednesday by remember { mutableStateOf("음식물 수거일") }
    var thursday by remember { mutableStateOf("대형 폐기물") }
    var friday by remember { mutableStateOf("대형 폐기물") }
    var saturday by remember { mutableStateOf("대형 폐기물") }
    var sunday by remember { mutableStateOf("대형 폐기물") }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(29.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        // Progress Bar
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .padding(7.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = "Progress Bar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(42.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                // 퍼센트 박스
                Row(
                    modifier = Modifier
                        .width(50.dp)
                        .height(42.dp)
                        .background(color = NeonBlue, shape = RoundedCornerShape(10.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${progressPercent}%",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                // 바 전체 틀
                Column(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color(0xFF79ECF4), shape = RoundedCornerShape(13.125.dp))
                        .width(285.dp)
                        .height(42.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(13.125.dp))
                        .padding(4.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    // 파란 progress bar
                    Row(
                        modifier = Modifier
                            .width((285.dp * progressFraction))
                            .height(34.dp)
                            .background(color = NeonBlue, shape = RoundedCornerShape(10.dp))
                            .padding(8.dp),
                    ) { }
                }
            }
        }

        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFF79ECF4),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("쓰레기 버리는 날", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("우리 동네 쓰레기 수거일을 등록해보세요", fontSize = 16.sp, color = Color(0xFF656565))
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GarbageDayItem("월", monday) { monday = it }
                GarbageDayItem("화", tuesday) { tuesday = it }
                GarbageDayItem("수", wednesday) { wednesday = it }
                GarbageDayItem("목", thursday) { thursday = it }
                GarbageDayItem("금", friday) { friday = it }
                GarbageDayItem("토", saturday) { saturday = it }
                GarbageDayItem("일", sunday) { sunday = it }
            }
        }

        // 목표 + 체크박스
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Mint, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("challenge") }
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFF79ECF4),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("나만의 목표", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("나만의 목표 등록 및 달성 기록", fontSize = 16.sp, color = Color(0xFF656565))
            }

            Column(
                modifier = Modifier
                    .background(color = Color(0xEBFFFFFF), shape = RoundedCornerShape(15.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                // ★ [수정] uiList 사용
                uiList.forEach { item ->
                    RecordGoalItem(
                        text = item.challenge.title,
                        checked = item.isDoneToday,
                        onCheckedChange = { checked ->
                            viewModel.toggleCheck(item.challenge, checked)
                        }
                    )
                }
            }
        }
    }
}