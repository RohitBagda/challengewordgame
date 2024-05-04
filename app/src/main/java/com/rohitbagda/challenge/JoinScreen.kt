package com.rohitbagda.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JoinScreen(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: ChallengeViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        JoinRoomView(
            navigateToGameRoomScreen = navigateToGameRoomScreen,
            navigateBack = navigateBack,
            viewModel = viewModel
        )
    }
}

@Composable
fun JoinRoomView(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: ChallengeViewModel
) {
    var roomCode by remember { mutableStateOf("") }
    var invalidRoomCode by remember { mutableStateOf(false) }
    var roomExists by remember { mutableStateOf(true) }
    fun loadRoom(text: String) {
        invalidRoomCode = text.length != 6
        if (!invalidRoomCode) {
            viewModel.loadGame(text)
        }
    }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = roomCode,
            onValueChange = {
                roomCode = it.uppercase()
                roomExists = true
                loadRoom(roomCode)
            },
            modifier = Modifier.fillMaxWidth().padding(),
            supportingText = {
                if (invalidRoomCode) {
                    Text(text = "Game room code must have 6 alphanumeric characters")
                }
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = 28.sp
            ),
            singleLine = true,
            label = { Text(text = "Enter room code") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            trailingIcon = @Composable {
                if (invalidRoomCode) {
                    IconButton(
                        onClick = { /* Do Nothing */ },
                        content = @Composable { Icon(Icons.Outlined.Warning, "error", tint = MaterialTheme.colorScheme.error) }
                    )
                }
            },
            leadingIcon = @Composable {
                if (roomCode.length > 1) {
                    IconButton(
                        onClick = { roomCode = "" },
                        content = @Composable { Icon(Icons.Outlined.Clear, "clear") }
                    )
                }
            },
            keyboardActions = KeyboardActions { loadRoom(roomCode) },
            isError = invalidRoomCode
        )

        if (!roomExists) {
            Text(
                modifier = Modifier.padding(),
                text = "Room with code $roomCode does not exist!",
                color = Color.Red
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navigateBack()
                },
                modifier = Modifier.padding(10.dp),
            ) {
                Text(text = "Back")
            }
            Button(
                onClick = {
                    if (!invalidRoomCode) {
                        roomExists = viewModel.currentGame != null
                        if (roomExists && viewModel.hasStarted() != true) {
                            viewModel.addPlayer(gameRoomCode = roomCode, player = UserGenerator.generate(isHost = false))
                            navigateToGameRoomScreen()
                        }
                    }
                },
                modifier = Modifier.padding(10.dp),
            ) {
                Text(text = "Submit")
            }
        }
    }
}