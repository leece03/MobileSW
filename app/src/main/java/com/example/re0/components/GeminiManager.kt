package com.example.re0.components

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Gemini API 호출을 관리하는 싱글턴 객체
 */
object GeminiManager {
    // 1. API 키 설정 (보안상 BuildConfig 사용 권장)
    // 예: private const val API_KEY = BuildConfig.GEMINI_API_KEY
    private const val API_KEY = "AIzaSyDIvvJCdjkQlWDJmEkhkTEVlaZTM7_0xpc"

    // 2. 모델 초기화 (싱글턴 인스턴스로 관리됨)
    // gemini-1.5-flash 또는 gemini-1.5-pro 등 사용 가능
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = API_KEY
    )

    // 3. 비동기 요청 함수
    // suspend 키워드로 코루틴 내에서 실행됨을 명시
    suspend fun getResponse(prompt: String): String? {
        return withContext(Dispatchers.IO) { // 네트워크 작업은 IO 스레드에서 실행
            try {
                val response: GenerateContentResponse = generativeModel.generateContent(prompt)
                response.text // 결과 텍스트 반환
            } catch (e: Exception) {
                e.printStackTrace()
                "오류가 발생했습니다: ${e.message}"
            }
        }
    }
}