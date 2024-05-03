package com.rohitbagda.challenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    Surface {
        RoomCodeTextField(
            navigateToGameRoomScreen = navigateToGameRoomScreen,
            navigateBack = navigateBack,
            viewModel = viewModel
        )
    }
}

@Composable
fun RoomCodeTextField(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: ChallengeViewModel
) {
    var roomCode by remember { mutableStateOf("") }
    var invalidRoomCode by remember { mutableStateOf(false) }
    var roomExists by remember { mutableStateOf(true) }
    fun validate(text: String) { invalidRoomCode = text.length != 6 }

    Column {
        TextField(
            value = roomCode,
            onValueChange = {
                roomCode = it.uppercase()
                roomExists = true
                validate(roomCode)
            },
            modifier = Modifier.width(300.dp),
            supportingText = {
                if (invalidRoomCode) {
                    Text(text = "usernames can only be 4-12 charachters")
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
                        onClick = { roomCode = "" },
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
            keyboardActions = KeyboardActions { validate(roomCode) },
            isError = invalidRoomCode
        )

        if (!roomExists) {
            Text(text = "Room with code $roomCode does not exist!", color = Color.Red)
        }

        Row {
            Button(onClick = { navigateBack() }) {
                Text(text = "Back")
            }
            Button(
                onClick = {
                    if (!invalidRoomCode) {
                        viewModel.loadGame(gameRoomCode = roomCode)
                        roomExists = viewModel.currentGame != null
                        if (roomExists) {
                            navigateToGameRoomScreen()
                        }
                    }
                }
            ) {
                Text(text = "Submit")
            }
        }
    }
}