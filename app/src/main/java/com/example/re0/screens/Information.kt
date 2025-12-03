package com.example.re0.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.re0.R
import com.example.re0.components.CardTemplate
import com.example.re0.ui.theme.SkyBlue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import com.example.re0.data.ChatMessage
import com.example.re0.viewModel.ChatViewModel

data class TrashInfoData(
    val title: String,
    val guide: String,
    val caution: String
)
// [새로 추가] 챗봇 대화 팝업 UI
@Composable
fun ChatDialog(
    viewModel: ChatViewModel,
    onDismiss: () -> Unit
) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(350.dp)
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 상단 타이틀
                Text(
                    text = "제로웨이스트 AI 챗봇",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 대화 내용 (스크롤)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // 남은 공간 차지
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    reverseLayout = false
                ) {
                    items(chatHistory) { message ->
                        ChatBubble(message) // 앞서 만든 말풍선 UI 사용
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 입력창 영역
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("질문을 입력하세요", fontSize = 14.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendPrompt(inputText)
                                inputText = ""
                            }
                        },
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (isLoading) Color.Gray else SkyBlue
                        )
                    }
                }
            }
        }
    }
}

// [새로 추가] 말풍선 디자인
@Composable
fun ChatBubble(message: ChatMessage) {
    val align = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor = if (message.isUser) SkyBlue else Color.White
    val textColor = if (message.isUser) Color.White else Color.Black
    val borderColor = if (message.isUser) SkyBlue else Color(0xFFE0E0E0)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(bgColor, RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = textColor, fontSize = 14.sp)
        }
    }
}
@Composable
fun TrashInfoSheet(
    info: TrashInfoData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = Color(0xFFE3F3FB), shape = RoundedCornerShape(10.dp))
            .width(380.dp)
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Color(0xFFE3F3FB), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .padding(vertical = 5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = info.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Text(
            text = "버리는 요령",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )

        Text(
            text = info.guide,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF656565),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "주의사항",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp)
        )

        Text(
            text = info.caution,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF656565),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun InfoScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        InformationContent(innerPadding)
    }
}

@Composable
fun ClickableImageRow(
    icons: List<Int>,
    onClick: (index: Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icons.forEachIndexed { index, icon ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(74.dp)
                    .clickable { onClick(index) }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationContent(innerPadding: PaddingValues) {
    // 1. ViewModel 및 상태 선언
    val chatViewModel: ChatViewModel = viewModel()
    var showChatDialog by remember { mutableStateOf(false) }

    var showSheet by remember { mutableStateOf(false) }
    var sheetData by remember { mutableStateOf<TrashInfoData?>(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // 데이터 리스트 (기존과 동일)
    val allTrashItems = remember {
        listOf(
            TrashInfoData("종이", "책, 노트, 박스류: 테이프·스티커 제거 후 배출\n종이컵·종이팩: 깨끗이 헹구고 펼쳐서 배출", "영수증, 카페 종이컵의 코팅된 뚜껑/빨대는 일반쓰레기\n음식 오염 종이는 재활용 불가"),
            TrashInfoData("유리(병류)", "내용물 비우고 물로 간단히 헹구기\n라벨은 떼면 좋지만 필수는 아님", "깨진 유리는 재활용 불가 → 신문지로 감싸 일반쓰레기\n뚜껑은 재질별로 따로 (철·플라스틱·알루미늄)"),
            TrashInfoData("플라스틱", "PET, PP, HDPE 등 플라스틱 용기 비우고 헹구기\n라벨 제거 가능하면 제거(PET 병은 의무)", "색이 짙거나 불투명하면 재활용 효율 낮음\n음식물 기름 완전 제거 필요"),
            TrashInfoData("비닐류", "과자봉지, 라면봉지 등 씻기 힘든 비닐은 대부분 재활용 불가\n포장 비닐(깨끗하면 재활용 가능)만 배출", "오염된 비닐은 일반쓰레기\n은박코팅 비닐 → 재활용 불가"),
            TrashInfoData("캔류", "내용물 비우고 간단히 헹굼\n알루미늄캔/철캔 모두 재활용 가능", "스프레이류: 구멍 뚫지 말고 내용물 완전히 소진 후 배출\n부탄가스/살충제 → 전용 수거함 이용"),
            TrashInfoData("스티로폼", "로고, 테이프, 스티커 제거\n깨끗한 포장용 스티로폼만 재활용 가능", "음식물 묻은 배달용 스티로폼은 일반쓰레기\n건축용 단열재 스티로폼 → 재활용 불가"),
            TrashInfoData("의류", "헌옷수거함에 넣기 (깨끗하고 재사용 가능한 것들)", "젖은 옷, 찢어진 옷, 솜/이불류 → 일반쓰레기 또는 대형폐기물\n구두·가방은 의류가 아닌 일반잡화 → 별도 배출"),
            TrashInfoData("금속류(냄비·프라이팬 포함)", "씻어서 금속류로 배출", "플라스틱 손잡이는 제거 가능하면 제거\n코팅 벗겨진 후라이팬도 금속류로 가능"),
            TrashInfoData("형광등·전구", "아파트 단지 / 주민센터 / 근처 마트 전용 수거함 이용", "형광등은 유해물질 포함 → 일반쓰레기 절대 금지\nLED 전구는 일반쓰레기가 원칙(지자체 따라 다름)"),
            TrashInfoData("컴퓨터·전자제품", "대형 폐가전 무상배출 서비스 이용(컴퓨터/TV/냉장고 등)\n소형 전자제품은 종종 동 주민센터나 마트 수거함 있음", "무단배출 금지, 그냥 버리면 과태료\n데이터 저장장치(SSD/HDD)는 파손 후 배출 추천"),
            TrashInfoData("음식물 쓰레기", "물기 최대한 제거\n뼈·견과류 딱딱한 부분·티백·껍데기 일부는 음식물 아님", "닭뼈·소고기뼈·돼지뼈\n조개/게/새우 껍데기\n과일 껍질 중 딱딱한 것(파인애플·바나나 끝부분 등)"),
            TrashInfoData("대형 폐기물", "침대, 책상, 가전 등 큰 물건\n동사무소/구청 사이트에서 대형폐기물 스티커 구매 후 배출\n또는 폐가전 무상배출 신청", "무단으로 버리면 과태료")
        )
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
        ) {
            // 검색창 Row
            Row(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth()
                    .height(53.dp)
                    .background(Color(0xFFE3F3FB), RoundedCornerShape(48.dp))
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(text = "분리수거 검색하기", color = Color(0xFF656565)) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = Color(0xFF656565),
                        unfocusedPlaceholderColor = Color(0xFF656565),
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val query = searchQuery.text.trim()
                            if (query.isNotEmpty()) {
                                val match = allTrashItems.firstOrNull { item ->
                                    item.title.contains(query, ignoreCase = true)
                                }
                                if (match != null) {
                                    sheetData = match
                                    showSheet = true
                                }
                            }
                        }
                )
            }

            // 첫 번째 카드 (주요 분리수거 쓰레기)
            CardTemplate(
                modifier = Modifier.fillMaxWidth(),
                topColor = Color.White,
                bottomColor = SkyBlue,
                topContent = {
                    Text("주요 분리수거 쓰레기", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                },
                bottomContent = {
                    Column(
                        modifier = Modifier
                            .width(380.dp)
                            .height(300.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(23.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            ClickableImageRow(
                                icons = listOf(R.drawable.paper, R.drawable.glass, R.drawable.pet, R.drawable.plasticbag),
                                onClick = { index ->
                                    sheetData = when (index) {
                                        0 -> allTrashItems[0]; 1 -> allTrashItems[1]; 2 -> allTrashItems[2]; 3 -> allTrashItems[3]; else -> null
                                    }
                                    showSheet = sheetData != null
                                }
                            )
                            ClickableImageRow(
                                icons = listOf(R.drawable.can, R.drawable.st, R.drawable.cloth, R.drawable.ch),
                                onClick = { index ->
                                    sheetData = when (index) {
                                        0 -> allTrashItems[4]; 1 -> allTrashItems[5]; 2 -> allTrashItems[6]; 3 -> allTrashItems[7]; else -> null
                                    }
                                    showSheet = sheetData != null
                                }
                            )
                            ClickableImageRow(
                                icons = listOf(R.drawable.light, R.drawable.thing, R.drawable.food, R.drawable.big),
                                onClick = { index ->
                                    sheetData = when (index) {
                                        0 -> allTrashItems[8]; 1 -> allTrashItems[9]; 2 -> allTrashItems[10]; 3 -> allTrashItems[11]; else -> null
                                    }
                                    showSheet = sheetData != null
                                }
                            )
                        }
                    }
                }
            )

            // 두 번째 카드 (제로웨이스트 AI 문답) - 여기가 수정된 부분입니다
            CardTemplate(
                topColor = SkyBlue,
                bottomColor = Color.White,
                topContent = {
                    Text(
                        text = "제로웨이스트 AI 문답",
                        color = Color.Black,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                bottomContent = {
                    // 여기를 깔끔하게 하나의 Column으로 합쳤습니다.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable { showChatDialog = true }, // 클릭 이벤트
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "챗봇에게 물어보세요 (터치)",
                            fontSize = 16.sp,
                            color = Color(0xFF656565)
                        )

                        // 봇 아이콘과 말풍선
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.bot),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFFD9D9D9), RoundedCornerShape(46.dp))
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .background(Color(0xFFE3F3FB), RoundedCornerShape(13.dp))
                                    .padding(horizontal = 18.dp, vertical = 10.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "대형 폐기물 버리는 방법",
                                    fontSize = 16.sp,
                                    color = Color(0xFF656565)
                                )
                            }
                        }

                        // 하단 예시 질문
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .background(Color(0xFFE1E7E9), RoundedCornerShape(13.dp))
                                    .padding(start = 18.dp, top = 10.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Q 음식물이 묻은 배달 용기 버리는 방법",
                                    fontSize = 16.sp,
                                    color = Color(0xFF656565)
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    // 팝업 다이얼로그들 (Box 바깥에 위치해야 함)
    if (showSheet && sheetData != null) {
        Dialog(onDismissRequest = { showSheet = false }) {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                TrashInfoSheet(info = sheetData!!)
            }
        }
    }

    if (showChatDialog) {
        ChatDialog(
            viewModel = chatViewModel,
            onDismiss = { showChatDialog = false }
        )
    }
}