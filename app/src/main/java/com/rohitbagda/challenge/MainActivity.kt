package com.rohitbagda.challenge

import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.rohitbagda.challenge.ui.theme.ChallengewordgameTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notificationService = NotificationService(this)
        val viewModel = ChallengeViewModel(
            db = Firebase.database,
            notificationService = notificationService
        )
        // Set up lifecycle observer
        val appLifecycleObserver = AppLifecycleObserver(viewModel)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

        val wordSearchService = WordSearchService()
        wordSearchService.load(this)
        setContent {

            ChallengewordgameTheme {
                val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }

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
                            viewModel = viewModel,
                            wordSearchService = wordSearchService
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