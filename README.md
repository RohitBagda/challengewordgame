# Challenge Multiplayer Word Game

Challenge is a multiplayer word game application built for Android powered by Google Firebase's Realtime Database.

## Game Objectives and Rules

* A player hosts a game room.
* Up to 4 other players can join that room using a room code provided by the host.
* Each player chooses a letter on their turn to add to the "word".
* The game is over when the word is terminal 
  * i.e. no other letter can be appended to it such that the word acts as a prefix for one or more words.
* The goal of the game for each player is to ensure that the word doesn't end on their turn.
* For example: 
  * Let's assume there are 3 players they follow the same turn order:
    * Curious Cat
    * Buzzing Bee
    * Giddy Gorilla
  * Let's play the game:
    * Turn 1: Curious Cat chooses 'Q'
    * Turn 2: Buzzing Bee chooses 'U'
    * Turn 3: Giddy Gorilla chooses 'I'
    * Turn 4: Curious Cat chooses 'C'
    * Turn 5: Buzzing Bee chooses 'K'
    * Turn 6: Giddy Gorilla chooses 'L'
    * Turn 7: Curious Cat chooses 'Y' (Game Over since 'QUICKLY' cannot be extended any further)
  * Curious Cat Lost the game!
* A good vocabulary helps as if you cannot choose a letter to force the game to continue

## Why Google Firebase?
Google Firebase offers 
* Realtime Data Sync across multiple devices at the same time
* No server side code / websockets were needed for realtime communication!
* Easy Integration with Android, Apple and Web Apps and so it makes building the application for multiple platforms significantly easier. 
* No need for a local database on the endpoint.

## Features and Limitations of current implementation:
* Computer decides which word can be continued or not using a word list of ~70000 words sourced from [here.](https://websites.umich.edu/~jlawler/wordlist)
* Host controls when the game has started.
* New players cannot join the room after the game has started.
* Supports 20 concurrent games of up to 5 players (100 devices) due to limitations of Firebase's free Spark Plan for Realtime Database.
* If any player leaves the room after before the game is over, the game is abandoned.
* If any player force kills the app, the game room is in a corrupted state and all players need to kill the app and create a new game room to resume playing.
* App sends local notifications when the application is in the background.
* All Firebase interactions are handled by the `ChallengeViewModel`

## Future improvements
* Handle players leaving the room / force killing the app in the middle of the game gracefully.
* Allow players to supply their own username instead of the pre-canned ones supplied by the app.
* Improvements to the game over experience.
* Use Firebase Emulators to run realtime database locally for development use.
* Add Tests