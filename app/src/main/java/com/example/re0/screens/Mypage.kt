package com.example.re0.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.re0.R
import com.example.re0.components.MyProfile
import com.example.re0.model.Profile
import com.example.re0.viewModel.ProfileViewModel

@Composable
fun MypageScreen(navController: NavController,
           viewModel: ProfileViewModel = hiltViewModel()){
    Scaffold (
        topBar = { TopBar() },
        bottomBar ={ BottomBar()}
    ) {innerPadding ->
        val uiState = viewModel.uiState
        MyProfile(uiState = uiState)

        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,) {
            val uiState = viewModel.uiState
            MyProfile(uiState = uiState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyProfilePreview() {
    val fakeProfile = Profile(
        imageUrl = R.drawable.rectangle1_1,
        name = "홍길동",
        email = "test@example.com",
        achievements = listOf()
    )

    MyProfile(uiState = fakeProfile)
}