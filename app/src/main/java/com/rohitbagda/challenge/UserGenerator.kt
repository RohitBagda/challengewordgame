package com.rohitbagda.challenge

import java.util.UUID

object UserGenerator {
    fun generate(isHost: Boolean) = User(
        name = UsernameGenerator.generate(),
        isHost = isHost
    )
}