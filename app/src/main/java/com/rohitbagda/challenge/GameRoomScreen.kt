package com.rohitbagda.challenge

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun GameRoomScreen(navigateToHomeScreen: () -> Unit) {
    var messageState by remember { mutableStateOf("default") }
    var currentInput by remember { mutableStateOf("") }
    val database = Firebase.database
    val ref = database.getReference("message")
    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.getValue<String>()
            messageState = value?: "could not retrieve value"
            Log.d(ContentValues.TAG, "Value is: $value")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
        }
    })

    Column {
        Text(text = messageState)
        TextField(
            value = currentInput,
            onValueChange = { newValue -> currentInput = newValue },
            label = { Text(text = "Enter Text") }
        )
        Button(onClick = {
            ref.setValue(currentInput)
        }) {
            Text(text = "Submit")
        }
        Button(onClick = { navigateToHomeScreen() }) {
            Text(text = "End Session")
        }
    }
}