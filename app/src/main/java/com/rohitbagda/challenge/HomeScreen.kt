package com.rohitbagda.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.rohitbagda.challenge.ui.theme.ChallengewordgameTheme

@Composable
fun HomeScreen(
    navigateToJoinScreen: () -> Unit,
    navigateToGameRoomScreen: () -> Unit,
    viewModel: ChallengeViewModel
) {
    Surface {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                viewModel.createNewGame()
                navigateToGameRoomScreen()
            }) {
                Text(text = "Host")
            }
            Button(onClick = { navigateToJoinScreen() }) {
                Text(text = "Join")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ChallengewordgameTheme {
        HomeScreen(
            navigateToJoinScreen = {},
            navigateToGameRoomScreen = {},
            viewModel = ChallengeViewModel(Firebase.database)
        )
    }
}