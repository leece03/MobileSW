package com.example.re0.components

// 👇 1. BuildConfig를 사용하기 위해 import 필수
import com.example.re0.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Gemini API 호출을 관리하는 싱글턴 객체 (멀티턴 채팅 지원)
 */
object GeminiManager {
    // 2. API 키 설정 (이제 BuildConfig에서 안전하게 가져옵니다!)
    // Gradle 설정 덕분에 이제 소스코드에 키가 노출되지 않습니다.
    private val API_KEY = BuildConfig.GEMINI_API_KEY

    // 3. 안전 설정 (403 오류 방지)
    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH)
    )

    // 4. 모델 초기화
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash", // 404 방지를 위한 안정적인 버전
        apiKey = API_KEY,
        safetySettings = safetySettings
    )

    // 5. 대화 세션 저장소
    private var chat: Chat? = null

    /**
     * 사용자의 메시지를 보내고 응답을 받습니다. (멀티턴)
     */
    suspend fun getResponse(prompt: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                // 채팅 세션이 없으면 새로 시작
                if (chat == null) {
                    chat = generativeModel.startChat(
                        history = listOf()
                    )
                }

                val response = chat!!.sendMessage(prompt)
                response.text
            } catch (e: Exception) {
                e.printStackTrace()
                "오류가 발생했습니다: ${e.message}"
            }
        }
    }

    fun clearHistory() {
        chat = null
    }
}