package com.jarvis.wablast
import kotlin.random.Random

object DelayManager {
    fun nextDelay(): Long = Random.nextLong(5000, 15000)
}
