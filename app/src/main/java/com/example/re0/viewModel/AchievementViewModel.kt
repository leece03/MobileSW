package com.example.re0.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re0.model.Achievement
import com.example.re0.repository.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: MypageRepository
) : ViewModel() {

    private val _achievementList = MutableStateFlow<List<Achievement>>(emptyList())
    val achievementList: StateFlow<List<Achievement>> = _achievementList

    init {
        loadAchievements()
    }

    fun loadAchievements() {
        viewModelScope.launch {
            _achievementList.value = repository.getAllAchievement()
        }
    }

    fun markDone(id: Int, checked: Boolean) {
        viewModelScope.launch {
            if (checked) repository.markDone(id)
            loadAchievements()
        }
    }
}
