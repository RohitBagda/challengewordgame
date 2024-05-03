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
import java.util.UUID

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
            addPlayer(
                gameRoomCode = game.roomCode!!,
                player = UserGenerator.generate(isHost = true)
            )
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

    fun loadGame(gameRoomCode: String) {
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

    fun addPlayer(gameRoomCode: String, player: User)  {
        db.getReference(gameRoomCode).child("players").child(UUID.randomUUID().toString()).setValue(player)
        // Copy forces a redraw of if the Game's child properties change
        currentGame = currentGame?.copy()
    }

    fun updateWord(gameRoomCode: String, newWord: String) {
        db.getReference(gameRoomCode).child("currentWord").setValue(newWord)
        // Copy forces a redraw of if the Game's child properties change
        currentGame = currentGame?.copy()
    }
}

data class Game(
    var roomCode: String? = null,
    var roomLocked: Boolean? = null,
    var currentWord: String? = null,
    var players: MutableMap<String, User> = HashMap()
)

data class User(
    var name: String? = null,
    var isHost: Boolean? = null
)