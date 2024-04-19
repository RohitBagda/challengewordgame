package com.rohitbagda.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rohitbagda.challenge.ui.theme.ChallengewordgameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengewordgameTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.HOME_SCREEN.value) {
                    composable(route = Route.HOME_SCREEN.value) {
                        HomeScreen(
                            navigateToUsernameScreen = { navController.navigate(Route.USERNAME_SCREEN.value) },
                            navigateToJoinScreen = { navController.navigate(Route.JOIN_SCREEN.value) }
                        )
                    }
                    composable(route = Route.JOIN_SCREEN.value) {
                        JoinScreen(
                            navigateToUsernameScreen = { navController.navigate(Route.USERNAME_SCREEN.value) },
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Route.USERNAME_SCREEN.value) {
                        UsernameScreen(
                            navigateToGameRoomScreen = { navController.navigate(Route.GAME_ROOM_SCREEN.value) },
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Route.GAME_ROOM_SCREEN.value) {
                        GameRoomScreen(
                            navigateToHomeScreen = { navController.navigate(Route.HOME_SCREEN.value) }
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
    USERNAME_SCREEN("usernameScreen"),
    GAME_ROOM_SCREEN("gameRoomScreen")
}