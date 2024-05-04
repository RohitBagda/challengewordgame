package com.rohitbagda.challenge

object GameRoomCodeGenerator {
    private val alphaNumeric = (('A'..'Z') + (0..9))
    fun getCode() = List(6) { alphaNumeric.random() }.joinToString(separator = "")
}