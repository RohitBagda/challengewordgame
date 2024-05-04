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

class ChallengeViewModel(
    private val db: FirebaseDatabase,
    private val notificationService: NotificationService,
): ViewModel() {
    var currentGame: Game? by mutableStateOf(null)
    var user: User? by mutableStateOf(null)
    var isAppInBackground: Boolean by mutableStateOf(false)

    fun getCurrentGameRoomCode() = currentGame?.roomCode
    fun getCurrentGameWord() = currentGame?.currentWord
    private fun getCurrentGameWordLength() = currentGame?.currentWord?.length?: 0
    fun getCurrentGamePlayer() = currentGame?.currentPlayer
    fun getCurrentGamePlayers() = currentGame?.players?.values?: emptyList()
    fun getTurnQueue() = currentGame?.turnQueue
    fun getUserName() = user?.name
    fun isUserHost() = user?.isHost == true
    fun isUsersTurn() = user?.id == getCurrentGamePlayer()?.id
    fun hasStarted() = currentGame?.hasStarted
    private fun hasEnded() = currentGame?.hasEnded
    fun gameIsActive() = hasStarted() == true && hasEnded() == false

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
            ref.addValueEventListener(gameEventListener())
        }
    }

    fun loadGame(gameRoomCode: String) {
        val ref = db.getReference(gameRoomCode)
        ref.addValueEventListener(gameEventListener())
    }

    fun gameOver(gameRoomCode: String, wordSearchResult: WordSearchResult) {
        currentGame?.hasEnded = true
        db.getReference(gameRoomCode).child("hasEnded").setValue(true)
        db.getReference(gameRoomCode).child("wordSearchResult").setValue(wordSearchResult)
        currentGame =  currentGame?.copy()
    }

    fun endGame(gameRoomCode: String?) {
        if (gameRoomCode != null) {
            db.getReference(gameRoomCode).removeValue()
            currentGame = null
        }
    }

    private fun gameEventListener() = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.getValue<Game>()
            currentGame = value
            checkAndNotifyUserWhenOnBackground()
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
        }
    }

    private fun checkAndNotifyUserWhenOnBackground() {
        if (isAppInBackground && gameIsActive() && getCurrentGameWordLength() > 0) {
            notificationService.showTurnNotification()
        }
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

    fun updateWordAndTurn(gameRoomCode: String, newWord: String, player: User) {
        val gameRef = db.getReference(gameRoomCode)
        getTurnQueue()?.removeFirst()
        getTurnQueue()?.add(player)
        currentGame?.currentPlayer = getTurnQueue()?.first()
        gameRef.child("currentWord").setValue(newWord)
        gameRef.child("turnQueue").setValue(currentGame?.turnQueue)
        gameRef.child("currentPlayer").setValue(currentGame?.currentPlayer)
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
        currentGame?.hasStarted = true
        db.getReference(gameRoomCode).child("hasStarted").setValue(true)
        db.getReference(gameRoomCode).child("turnQueue").setValue(currentGame?.turnQueue)
        db.getReference(gameRoomCode).child("currentPlayer").setValue(currentGame?.currentPlayer)
        currentGame = currentGame?.copy()
    }
}

data class Game(
    var roomCode: String? = null,
    var hasStarted: Boolean? = false,
    var hasEnded: Boolean? = false,
    var currentWord: String? = null,
    var host: User? = null,
    var currentPlayer: User? = null,
    var players: MutableMap<String, User> = HashMap(),
    var turnQueue: MutableList<User> = mutableListOf(),
    var wordSearchResult: WordSearchResult? = null,
)

data class User(
    val id: String? = null,
    val name: String? = null,
    val isHost: Boolean? = null
)

data class WordSearchResult(
    val result: String? = null,
    val children: List<String> = mutableListOf()
)

object SearchResult {
    const val DOES_NOT_EXIST = "DOES_NOT_EXIST"
    const val HAS_NO_CHILDREN = "HAS_NO_CHILDREN"
    const val HAS_NO_CHILDREN_NOT_YET_COMPLETE = "HAS_NO_CHILDREN_NOT_YET_COMPLETE"
    const val HAS_CHILDREN = "HAS_CHILDREN"
}