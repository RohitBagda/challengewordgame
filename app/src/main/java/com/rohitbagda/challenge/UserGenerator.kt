package com.rohitbagda.challenge

import java.util.UUID

object UserGenerator {
    fun generate(isHost: Boolean) = User(
        id = UUID.randomUUID().toString(),
        name = UsernameGenerator.generate(),
        isHost = isHost
    )
}