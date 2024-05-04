package com.rohitbagda.challenge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameRoomScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: ChallengeViewModel,
    wordSearchService: WordSearchService
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (viewModel.currentGame?.hasEnded != true) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CHALLENGE",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(40.0F, type = TextUnitType.Sp)
                )
                Text(text = "roomCode = ${viewModel.getCurrentGameRoomCode()}")
                Text(text = "username = ${viewModel.getUserName()} ${if(viewModel.isUserHost()) { "(Host)" } else { "" }} ")
                Text(text = "playerList = ${viewModel.getCurrentGamePlayers().joinToString { it.name?: "" }}")
                if (viewModel.hasStarted() == true) {
                    Text(text = "turnOrder = ${viewModel.getTurnQueue()?.asIterable()?.joinToString { it.name?: "" }}")
                    Text(text = "currentPlayer = ${viewModel.getCurrentGamePlayer()?.name}")
                    Text(text = "currentWord = ${viewModel.getCurrentGameWord()}")
                }
                ControlsView(viewModel, navigateToHomeScreen)
                OnScreenKeyboard {
                    if (viewModel.getCurrentGameRoomCode() != null && viewModel.isUsersTurn()) {
                        val newWord = viewModel.getCurrentGameWord()!! + it
                        if (viewModel.getCurrentGameWord() != null) {
                            val searchResult = wordSearchService.wordCanBeContinued(newWord)
                            if (searchResult.result != SearchResult.HAS_CHILDREN && searchResult.result != SearchResult.HAS_NO_CHILDREN_NOT_YET_COMPLETE) {
                                viewModel.gameOver(viewModel.getCurrentGameRoomCode()!!, searchResult)
                            }
                        }
                        
                        if (viewModel.currentGame?.hasEnded != true) {
                            viewModel.updateWordAndTurn(
                                gameRoomCode = viewModel.getCurrentGameRoomCode()!!,
                                newWord = newWord,
                                player = viewModel.user!!
                            )
                        }
                    }
                }
            }
        }
        if (viewModel.currentGame?.hasEnded == true) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CHALLENGE",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(40.0F, type = TextUnitType.Sp)
                )
                Text(text = "Game Over!")
                Text(text = "${viewModel.currentGame?.currentPlayer?.name} has lost")
                ControlsView(viewModel, navigateToHomeScreen)
            }
        }
    }
}

@Composable
private fun ControlsView(
    viewModel: ChallengeViewModel,
    navigateToHomeScreen: () -> Unit
) {
    when {
        viewModel.isUserHost() -> ShowHostControls(
            viewModel = viewModel,
            navigateToHomeScreen = navigateToHomeScreen
        )

        else -> ShowNonHostControls(
            viewModel = viewModel,
            navigateToHomeScreen = navigateToHomeScreen
        )
    }
}

@Composable
private fun ShowNonHostControls(viewModel: ChallengeViewModel, navigateToHomeScreen: () -> Unit) {
    Button(onClick = {
        navigateToHomeScreen()
    }) {
        Text(text = "Exit Game")
    }
}

@Composable
private fun ShowHostControls(
    viewModel: ChallengeViewModel,
    navigateToHomeScreen: () -> Unit
) {
    var startGameEnabled by remember { mutableStateOf(false) }
    startGameEnabled = viewModel.gameHasEnoughPlayers() && viewModel.hasStarted() == false
    Row(
        modifier = Modifier.fillMaxWidth().padding(),
        horizontalArrangement = Arrangement.Center
    )  {
        Button(
            onClick = {
                viewModel.lockRoom(gameRoomCode = viewModel.getCurrentGameRoomCode()!!)
                viewModel.createTurnOrder(gameRoomCode = viewModel.getCurrentGameRoomCode()!!)
                startGameEnabled = false
            },
            enabled = startGameEnabled,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Start Game")
        }
        Button(
            onClick = {
                viewModel.endGame(gameRoomCode = viewModel.getCurrentGameRoomCode())
                navigateToHomeScreen()
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = if (viewModel.isUserHost()) "End Game" else "Exit Game")
        }
    }
}

@Composable
fun OnScreenKeyboard(onLetterTyped: (Char) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
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
    Box(
        Modifier
            .height(50.dp)
            .width(35.dp)
            .clip(RoundedCornerShape(10))
            .clickable(onClick = { onClick(letter) }), Alignment.Center) {
        Text(
            modifier = Modifier,
            text = letter.toString(),
            color = Color.Black,
            fontSize = 24.sp
        )
    }
}
