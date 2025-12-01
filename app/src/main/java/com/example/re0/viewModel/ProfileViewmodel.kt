package com.example.re0.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.R
import com.example.re0.model.Achievement
import com.example.re0.model.Profile
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    var uiState by mutableStateOf(
        Profile(
            imageUrl = R.drawable.rectangle1_2,
            name = "",
            email = ""
        )
    )
        private set

    var achievementsState by mutableStateOf<List<Achievement>>(emptyList())
        private set

    init {
        loadProfile()
        loadAchievements() // 독립적으로 동작
    }

    fun badgeCount(): Int = achievementsState.count { it.isBadge }

    private fun loadAchievements() {
        viewModelScope.launch {
            achievementsState = repository.getAllAchievement()
            Log.d("ProfileVM", "allAchievements = ${repository.getAllAchievement()}")

        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            var loaded = repository.getProfile()
            if (loaded == null) {
                repository.insertProfile(
                    Profile(
                        imageUrl = R.drawable.earth,
                        name = "홍길동",
                        email = "hong@test.com"
                    )
                )
                loaded = repository.getProfile()
            }


            loaded?.let {
                uiState = it
            }
            achievementsState = repository.getAllAchievement()

            Log.d("ProfileVM", "loadProfile() = $loaded")
        }
    }

    fun updateUserProfile(name: String, email: String) {
        val updated = uiState.copy(name = name, email = email)
        viewModelScope.launch {
            repository.updateProfile(updated)
            uiState = updated
        }
    }

    fun addAchievement(achievement: Achievement) {
        viewModelScope.launch {
            repository.insertAchievement(achievement)
            loadAchievements() // ⭐ UI 즉각 반영
        }
    }
}