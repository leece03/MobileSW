package com.example.re0.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onFail("이메일 또는 비밀번호가 비어 있습니다.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFail(task.exception?.localizedMessage ?: "로그인 실패")
                }
            }
    }


    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onFail("이메일 또는 비밀번호가 비어 있습니다.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFail(task.exception?.localizedMessage ?: "회원가입 실패")
                }
            }
    }
}
