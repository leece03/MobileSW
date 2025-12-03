package com.example.re0.data

data class ChatMessage(
    val text: String,      // 메시지 내용
    val isUser: Boolean    // 누가 보냈는지 (true: 사용자, false: 제미나이 봇)
)