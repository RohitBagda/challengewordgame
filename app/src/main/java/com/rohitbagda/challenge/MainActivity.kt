package com.rohitbagda.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.rohitbagda.challenge.ui.theme.ChallengewordgameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ChallengeViewModel(Firebase.database)
        setContent {
            ChallengewordgameTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.HOME_SCREEN.value) {
                    composable(route = Route.HOME_SCREEN.value) {
                        HomeScreen(
                            navigateToJoinScreen = { navController.navigate(Route.JOIN_SCREEN.value) },
                            navigateToGameRoomScreen = { navController.navigate(Route.GAME_ROOM_SCREEN.value) },
                            viewModel = viewModel
                        )
                    }
                    composable(route = Route.JOIN_SCREEN.value) {
                        JoinScreen(
                            navigateToGameRoomScreen = { navController.navigate(Route.GAME_ROOM_SCREEN.value) },
                            navigateBack = { navController.popBackStack() },
                            viewModel = viewModel
                        )
                    }
                    composable(route = Route.GAME_ROOM_SCREEN.value) {
                        GameRoomScreen(
                            navigateToHomeScreen = { navController.navigate(Route.HOME_SCREEN.value) },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

enum class Route(val value: String) {
    HOME_SCREEN("homeScreen"),
    JOIN_SCREEN("joinScreen"),
    GAME_ROOM_SCREEN("gameRoomScreen")
}