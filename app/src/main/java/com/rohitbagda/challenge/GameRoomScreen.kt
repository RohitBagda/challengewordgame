package com.rohitbagda.challenge

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun GameRoomScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: ChallengeViewModel
) {
    var currentInput by remember { mutableStateOf("") }
    Column {
        Text(text = viewModel.currentGame?.roomCode?: "")
        Text(text = viewModel.currentGame?.currentWord?: "")
        TextField(
            value = currentInput,
            onValueChange = { newValue -> currentInput = newValue },
            label = { Text(text = "Enter Text") }
        )
        Button(onClick = {
            if (viewModel.currentGame?.roomCode != null) {
                viewModel.updateWord(gameRoomCode = viewModel.currentGame?.roomCode!!, newWord = currentInput )
            }
        }) {
            Text(text = "Submit")
        }
        Button(onClick = { navigateToHomeScreen() }) {
            Text(text = "End Session")
        }
    }
}