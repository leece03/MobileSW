package com.example.re0.viewModel // 패키지명 확인!

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.components.GeminiManager
import com.example.re0.data.ChatMessage // 아까 만든 ChatMessage import 필요
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // 여기가 핵심! 대화 목록을 저장하는 리스트 변수입니다.
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun sendPrompt(userPrompt: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. 사용자 질문을 리스트에 추가
            addMessage(userPrompt, true)

            // 2. 비동기로 API 요청 (GeminiManager가 있어야 함)
            // GeminiManager 코드가 없다면 이 줄에서 에러가 날 수 있습니다.
            val response = GeminiManager.getResponse(userPrompt)

            // 3. 응답이 오면 리스트에 추가
            addMessage(response ?: "오류 발생", false)

            _isLoading.value = false
        }
    }

    // 리스트에 메시지를 추가하는 헬퍼 함수
    private fun addMessage(text: String, isUser: Boolean) {
        val newMessage = ChatMessage(text, isUser)
        _chatHistory.value = _chatHistory.value + newMessage
    }
}