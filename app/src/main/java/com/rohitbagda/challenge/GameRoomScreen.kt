package com.rohitbagda.challenge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
        if (viewModel.currentGame == null) {
            navigateToHomeScreen()
        }

        if (viewModel.currentGame?.hasEnded != true) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                ChallengeTitleView()
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Room Code",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                )
                Text(
                    text = viewModel.getCurrentGameRoomCode()?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(30.0F, type = TextUnitType.Sp)
                )
                Text(
                    text = if (viewModel.isUserHost()) "You are the Host!" else "Hosted by: ${viewModel.currentGame?.host?.name}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                )
                Text(
                    text = "Your username",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                )
                Text(
                    text = viewModel.getUserName()?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(25.0F, type = TextUnitType.Sp)
                )

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(30.dp)
                ) {
                    Text(
                        text = "Players in room",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                    )
                    viewModel.getCurrentGamePlayers().mapNotNull { it.name }.forEach {
                        Text(
                            text = it,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = TextUnit(20.0F, type = TextUnitType.Sp)
                        )
                    }
                }

                if (viewModel.hasStarted() == true) {
                    Text(
                        text = "Turn",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                    )
                    Text(
                        text = if (viewModel.isUsersTurn()) "It's your turn!" else "${viewModel.currentGame?.currentPlayer?.name}'s turn!",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(25.0F, type = TextUnitType.Sp)
                    )
                    Text(
                        text = "Word",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                    )
                    if (viewModel.getCurrentGameWord()?.length == 0 && viewModel.isUsersTurn()) {
                        Text(
                            text = "Choose a letter to start the game",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = TextUnit(15.0F, type = TextUnitType.Sp)
                        )
                    }

                    Text(
                        text = viewModel.getCurrentGameWord()?: "",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(25.0F, type = TextUnitType.Sp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
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
        }
        if (viewModel.currentGame?.hasEnded == true) {
            Column(
                modifier = Modifier.padding(30.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                ChallengeTitleView()
                Text(
                    text = "Game Over!",
                    modifier = Modifier.fillMaxWidth().padding(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(30.0F, type = TextUnitType.Sp)
                )
                Text(
                    text = "${viewModel.currentGame?.currentPlayer?.name} has lost",
                    modifier = Modifier.fillMaxWidth().padding(),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(30.0F, type = TextUnitType.Sp)
                )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(onClick = {
            viewModel.endGame(gameRoomCode = viewModel.getCurrentGameRoomCode())
            navigateToHomeScreen()
        }) {
            Text(text = "End Game")
        }
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
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    )  {
        if (viewModel.currentGame?.hasEnded != true) {
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
