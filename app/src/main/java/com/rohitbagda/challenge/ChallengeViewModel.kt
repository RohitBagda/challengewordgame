package com.rohitbagda.challenge

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ChallengeViewModel(private val db: FirebaseDatabase): ViewModel() {
    var currentGame: Game? by mutableStateOf(null)

    fun getCurrentGameRoomCode() = currentGame?.roomCode
    fun getCurrentWord() = currentGame?.currentWord

    fun createNewGame() {
        val game = Game(roomCode = GameRoomCodeGenerator.getCode(), currentWord = "")
        if (game.roomCode != null) {
            val ref = db.getReference(game.roomCode!!)
            ref.setValue(game)
            currentGame = game
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue<Game>()
                    currentGame = value
                    Log.d(ContentValues.TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    fun fetchGame(gameRoomCode: String) {
        val ref = db.getReference(gameRoomCode)
        ref.get().addOnSuccessListener {
            currentGame = it.getValue<Game>()
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Game>()
                currentGame = value
                Log.d(ContentValues.TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun updateWord(gameRoomCode: String, newWord: String) {
        db.getReference(gameRoomCode).child("currentWord").setValue(newWord)
        currentGame?.currentWord = newWord
    }
}

data class Game(
    var roomCode: String? = null,
    var currentWord: String? = null,
)

data class User(
    var id: String,
    var name: String
)