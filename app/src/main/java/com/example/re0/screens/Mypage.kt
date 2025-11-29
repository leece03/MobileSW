package com.example.re0.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.re0.components.AchievementsGraph
import com.example.re0.components.MyBadge
import com.example.re0.components.MyProfile
import com.example.re0.viewModel.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MypageScreen(navController: NavController,
                 viewModel: ProfileViewModel = hiltViewModel(),
                 backStackEntry: NavBackStackEntry
){
    Scaffold (
        topBar = { TopBar() },
        bottomBar ={ BottomBar(navController)}
    ) {innerPadding ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()) ,
            horizontalAlignment = Alignment.CenterHorizontally,) {
            val uiState = viewModel.uiState
            val achievementsState=viewModel.achievementsState
            val badgeCount = viewModel.badgeCount()
            MyProfile(uiState = uiState,achievements = achievementsState)
            MyBadge(achievements = achievementsState,badgeCount)
            AchievementsGraph(achievements = achievementsState)
        }
    }
}
