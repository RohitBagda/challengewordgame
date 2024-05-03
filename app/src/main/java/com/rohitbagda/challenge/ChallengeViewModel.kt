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
    fun getCurrentGameWord() = currentGame?.currentWord
    fun getCurrentGamePlayer() = currentGame?.currentPlayer
    fun getCurrentGamePlayers() = currentGame?.players?.values?: emptyList()
    fun getTurnQueue() = currentGame?.turnQueue
    fun getGameHost() = currentGame?.host
    fun isUserHost() = user?.isHost == true
    fun isUsersTurn() = user?.id == getCurrentGamePlayer()?.id
    fun isRoomLocked() = currentGame?.roomLocked
    fun gameHasEnoughPlayers() = (currentGame?.players?.values?.size?: 0) > 1


    fun createNewGame() {
        val user = UserGenerator.generate(isHost = true)
        val game = Game(
            roomCode = GameRoomCodeGenerator.getCode(),
            currentWord = "",
            host = user,
            currentPlayer = null,
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
        currentGame?.turnQueue?.addAll((currentGame?.players?.values?: emptyList()))
        currentGame?.turnQueue?.shuffle()
        currentGame?.currentPlayer = currentGame?.turnQueue?.first()
        db.getReference(gameRoomCode).child("turnQueue").setValue(currentGame?.turnQueue)
        db.getReference(gameRoomCode).child("currentPlayer").setValue(currentGame?.currentPlayer)
        currentGame = currentGame?.copy()
    }

    fun updateTurnOrder(gameRoomCode: String, player: User) {
        getTurnQueue()?.removeFirst()
        getTurnQueue()?.add(player)
        currentGame?.currentPlayer = getTurnQueue()?.first()
        Log.i(ContentValues.TAG, "After Adding Player ${player.name}: currentPlayer = ${currentGame?.currentPlayer}, Turn Queue ${getTurnQueue()}")
        db.getReference(gameRoomCode).child("turnQueue").setValue(currentGame?.turnQueue)
        db.getReference(gameRoomCode).child("currentPlayer").setValue(currentGame?.currentPlayer)
        currentGame = currentGame?.copy()
    }
}

data class Game(
    var roomCode: String? = null,
    var roomLocked: Boolean? = false,
    var currentWord: String? = null,
    var host: User? = null,
    var currentPlayer: User? = null,
    var players: MutableMap<String, User> = HashMap(),
    var turnQueue: MutableList<User> = mutableListOf(),
)

data class User(
    val id: String? = null,
    val name: String? = null,
    val isHost: Boolean? = null
)