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
fun UsernameScreen(
    navigateToGameRoomScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    Surface {
        UsernameTextField(
            navigateToUsernameScreen = navigateToGameRoomScreen,
            navigateBack = navigateBack
        )
    }
}

@Composable
fun UsernameTextField(
    navigateToUsernameScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("rohit") }
    var usernameError by rememberSaveable { mutableStateOf(false) }
    fun validate(text: String) {
        usernameError = text.length !in 4..12
    }
    Column {
        TextField(
            value = username,
            onValueChange = {
                username = it.lowercase()
                validate(username)
            },
            modifier = Modifier.width(300.dp),
            supportingText = {
                if (usernameError) {
                    Text(text = "usernames can only be 4-12 charachters")
                }
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = 28.sp
            ),
            singleLine = true,
            label = { Text(text = "Enter username") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            trailingIcon = @Composable {
                if (usernameError) {
                    IconButton(
                        onClick = { username = "" },
                        content = @Composable { Icon(Icons.Outlined.Warning, "error", tint = MaterialTheme.colorScheme.error) }
                    )
                }
            },
            leadingIcon = @Composable {
                if (username.length > 1) {
                    IconButton(
                        onClick = { username = "" },
                        content = @Composable { Icon(Icons.Outlined.Clear, "clear") }
                    )
                }
            },
            keyboardActions = KeyboardActions { validate(username) },
            isError = usernameError
        )
        Row {
            Button(onClick = { navigateBack() }) {
                Text(text = "Back")
            }
            Button(onClick = { if (!usernameError) navigateToUsernameScreen() }) {
                Text(text = "Submit")
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun UsernameScreenPreview() {
    ChallengewordgameTheme {
        UsernameScreen( { }, { })
    }
}