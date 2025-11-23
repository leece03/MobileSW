package com.example.re0.screens


/*
@Composable
fun RecordScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        RecordContent(innerPadding)
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

@Composable
fun GoalItem(
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
                .weight(1f)   // ← width(307.dp)를 없애고 자동으로 꽉 차게!
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
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.4.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF656565),
                )
            )
        }
    }
}

@Composable
fun RecordContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(29.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,

    ) {
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
                        drawLine(
                            color = Color(0xFF79ECF4),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "챌린지 등록",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 33.6.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF000000),
                    )
                )
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "나만의 챌린지를 등록해 보세요",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF656565),
                    )
                )
                Column(
                    modifier = Modifier
                        .width(348.dp)
                        .height(42.dp)
                        .background(color = Color(0xFFE3F3FB), shape = RoundedCornerShape(size = 13.125.dp))
                        .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "입력하기",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.4.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF656565),
                        )
                    )
                }

            }

        }


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
                        drawLine(
                            color = Color(0xFF79ECF4),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "달성 기록",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 33.6.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF000000),
                    )
                )
                Text(
                    text = "나만의 목표 등록 및 달성 기록",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF656565),
                    )
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
                GoalItem(
                    text = "배달음식 안시켜먹기",
                    checked = false,
                    onCheckedChange = { /* TODO */ }
                )
                GoalItem(
                    text = "배달음식 안시켜먹기",
                    checked = false,
                    onCheckedChange = { /* TODO */ }
                )

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecordPreview() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar() },     // 실제 TopBar
        bottomBar = { BottomBar() } // 실제 BottomBar
    ) { innerPadding ->
        RecordContent(innerPadding)
    }
}


 */