package com.rohitbagda.challenge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameRoomScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: ChallengeViewModel
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(30.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "roomCode = ${viewModel.getCurrentGameRoomCode()}")
            Text(text = "username = ${viewModel.getUserName()}")
            Text(text = "host = ${viewModel.getGameHost()?.name}")
            Text(text = "playerList = ${viewModel.getCurrentGamePlayers().joinToString { it.name?: "" }}")
            if (viewModel.isRoomLocked() == true) {
                Text(text = "turnOrder = ${viewModel.getTurnQueue()?.asIterable()?.joinToString { it.name?: "" }}")
                Text(text = "currentPlayer = ${viewModel.getCurrentGamePlayer()?.name}")
                Text(text = "currentWord = ${viewModel.getCurrentGameWord()}")
            }
            when {
                viewModel.isUserHost() -> ShowHostControls(viewModel, navigateToHomeScreen)
                else -> ShowNonHostControls(navigateToHomeScreen)
            }
            OnScreenKeyboard {
                if (viewModel.getCurrentGameRoomCode() != null && viewModel.isUsersTurn()) {
                    viewModel.updateWordAndTurn(
                        gameRoomCode = viewModel.getCurrentGameRoomCode()!!,
                        newWord = viewModel.getCurrentGameWord() + it,
                        player = viewModel.user!!
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowNonHostControls(navigateToHomeScreen: () -> Unit) {
    Button(onClick = { navigateToHomeScreen() }) { Text(text = "Exit") }
}

@Composable
private fun ShowHostControls(
    viewModel: ChallengeViewModel,
    navigateToHomeScreen: () -> Unit
) {
    if (viewModel.isRoomLocked() == false && viewModel.gameHasEnoughPlayers()) {
        Button(onClick = {
            viewModel.lockRoom(gameRoomCode = viewModel.getCurrentGameRoomCode()!!)
            viewModel.createTurnOrder(gameRoomCode = viewModel.getCurrentGameRoomCode()!!)
        }) {
            Text(text = "Start Game")
        }
    }
    if (viewModel.isRoomLocked() == true) {
        Button(onClick = { navigateToHomeScreen() }) { Text(text = "End Game") }
    }
}

@Composable
fun OnScreenKeyboard(onLetterTyped: (Char) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display uppercase alphabet keys in a single row
        setOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM").forEach { keyboardRow ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                keyboardRow.forEach {
                    KeyboardKey(it) { letter ->
                        onLetterTyped(letter)
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardKey(letter: Char, onClick: (Char) -> Unit) {
    Box(Modifier.height(40.dp).width(20.dp).clickable(onClick = { onClick(letter) }), Alignment.Center) {
        Text(
            modifier = Modifier,
            text = letter.toString(),
            color = Color.Black,
            fontSize = 24.sp
        )
    }
}
