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
    var user: User? by mutableStateOf(null)

    fun getCurrentGameRoomCode() = currentGame?.roomCode
    fun getCurrentWord() = currentGame?.currentWord
    fun getGameHost() = currentGame?.host
    fun isRoomLocked() = currentGame?.roomLocked


    fun createNewGame() {
        val user = UserGenerator.generate(isHost = true)
        val game = Game(
            roomCode = GameRoomCodeGenerator.getCode(),
            currentWord = "",
            host = user
        )
        if (game.roomCode != null) {
            val ref = db.getReference(game.roomCode!!)
            ref.setValue(game)
            addPlayer(gameRoomCode = game.roomCode!!, player = user)
            currentGame = game
            this.user = user
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
        db.getReference(gameRoomCode)
            .child("players")
            .child(player.id?: UUID.randomUUID().toString())
            .setValue(player)
        // Copy forces a redraw of if the Game's child properties change
        currentGame = currentGame?.copy()
        user = player
    }

    fun updateWord(gameRoomCode: String, newWord: String) {
        db.getReference(gameRoomCode).child("currentWord").setValue(newWord)
        // Copy forces a redraw of if the Game's child properties change
        currentGame = currentGame?.copy()
    }

    fun lockRoom(gameRoomCode: String) {
        db.getReference(gameRoomCode).child("roomLocked").setValue(true)
        // Copy forces a redraw of if the Game's child properties change
        currentGame = currentGame?.copy()
    }

    fun createTurnOrder(gameRoomCode: String) {
        currentGame?.turnQueue?.addAll((currentGame?.players?.values?: emptyList()).shuffled())
        db.getReference(gameRoomCode).child("turnQueue").setValue(currentGame?.turnQueue)
    }
}

data class Game(
    var roomCode: String? = null,
    var roomLocked: Boolean? = false,
    var currentWord: String? = null,
    var players: MutableMap<String, User> = HashMap(),
    var turnQueue: MutableList<User> = mutableListOf(),
    var host: User? = null
)

data class User(
    var id: String? = null,
    var name: String? = null,
    var isHost: Boolean? = null
)