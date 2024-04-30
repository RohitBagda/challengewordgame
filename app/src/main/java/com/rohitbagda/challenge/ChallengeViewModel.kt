package com.rohitbagda.challenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ChallengeViewModel: ViewModel() {
    val currentWord: Game? by mutableStateOf(null)
}

data class Game(
    var roomCode: String,
    val currentWord: String,
)

data class User(
    var name: String
)