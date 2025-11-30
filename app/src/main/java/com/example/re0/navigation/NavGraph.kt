package com.example.re0.navigation

//import com.example.re0.screens.RecordScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.re0.screens.ChallengeScreen
import com.example.re0.screens.HomeScreen
import com.example.re0.screens.InfoScreen
import com.example.re0.screens.LoginScreen
import com.example.re0.screens.MapScreen
import com.example.re0.screens.MypageScreen
import com.example.re0.screens.RecordScreen
import com.example.re0.viewModel.AuthViewModel
import com.example.re0.viewModel.PlacesViewModel
import com.example.re0.viewModel.ProfileViewModel
import com.example.re0.viewModel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    val placeViewModle: PlacesViewModel = hiltViewModel()
    val profileViewModle: ProfileViewModel = hiltViewModel()
    val quizViewModel: QuizViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") {
            LoginScreen(navController,authViewModel)
        }
        composable("home") {backStackEntry ->
            HomeScreen(navController,quizViewModel,backStackEntry)
        }
        composable("info") {backStackEntry ->
           InfoScreen(navController,backStackEntry)
        }
        composable("history") {backStackEntry ->
            RecordScreen(navController,backStackEntry)
        }
        composable("mypage") {backStackEntry ->
            MypageScreen(navController,profileViewModle,backStackEntry)
        }
        composable("map") {backStackEntry ->
            MapScreen(navController,placeViewModle,backStackEntry)
        }
        composable("challenge") { backStackEntry ->
            ChallengeScreen(navController)
        }

    }
}
