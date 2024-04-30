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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohitbagda.challenge.ui.theme.ChallengewordgameTheme

@Composable
fun JoinScreen(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    Surface {
        RoomCodeTextField(
            navigateToGameRoomScreen = navigateToGameRoomScreen,
            navigateBack = navigateBack
        )
    }
}

@Composable
fun RoomCodeTextField(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    var roomCode by rememberSaveable { mutableStateOf("rohit") }
    var roomCodeError by rememberSaveable { mutableStateOf(false) }
    fun validate(text: String) {
        roomCodeError = text.length !in 4..12
    }

    Column {
        TextField(
            value = roomCode,
            onValueChange = {
                roomCode = it.lowercase()
                validate(roomCode)
            },
            modifier = Modifier.width(300.dp),
            supportingText = {
                if (roomCodeError) {
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
                if (roomCodeError) {
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
            isError = roomCodeError
        )
        Row {
            Button(onClick = { navigateBack() }) {
                Text(text = "Back")
            }
            Button(onClick = { if (!roomCodeError) navigateToGameRoomScreen() }) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinScreenPreview() {
    ChallengewordgameTheme {
        JoinScreen( {}, {} )
    }
}