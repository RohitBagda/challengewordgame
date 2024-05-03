package com.rohitbagda.challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameRoomScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: ChallengeViewModel
) {
    Column(Modifier.padding(30.dp)) {
        Text(text = viewModel.getCurrentGameRoomCode() ?: "")
        Text(text = "currentPlayers = ${viewModel.currentGame?.players?.values?.joinToString { it.name?: "" }}")
        Text(text = "currentWord = ${viewModel.currentGame?.currentWord?: ""}", color = Color.Black)
        Button(onClick = { navigateToHomeScreen() }) { Text(text = "End Session") }
        OnScreenKeyboard {
            if (viewModel.getCurrentGameRoomCode() != null) {
                viewModel.updateWord(
                    gameRoomCode = viewModel.getCurrentGameRoomCode()!!,
                    newWord = viewModel.getCurrentWord() + it
                )
            }
        }
    }
}

@Composable
fun OnScreenKeyboard(onLetterTyped: (Char) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            .height(40.dp)
            .width(20.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Transparent)
            .clickable(onClick = { onClick(letter) }), Alignment.Center)
    {
        Text(
            modifier = Modifier,
            text = letter.toString(),
            color = Color.Black,
            fontSize = 24.sp
        )
    }
}
