package com.rohitbagda.challenge

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun ChallengeTitleView() {
    Text(
        text = "CHALLENGE",
        modifier = Modifier.fillMaxWidth().padding(30.dp),
        textAlign = TextAlign.Center,
        fontSize = TextUnit(40.0F, type = TextUnitType.Sp)
    )
}