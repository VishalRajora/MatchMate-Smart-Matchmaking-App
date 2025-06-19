package com.example.matchmate.util

import android.util.Log
import kotlinx.coroutines.delay
import java.io.IOException


suspend fun <T> simulateFlaky(block: suspend () -> T): T {
    val chance = (1..100).random()
    if (chance <= 30) { // 30% chance to simulate a flaky failure
        throw IOException("Simulated flaky failure")
    }
    return block()
}

suspend fun <T> retry(
    times: Int = 3,
    delayMillis: Long = 1000,
    block: suspend () -> T,
    blockError: suspend () -> T,
): T {
    var lastError: Throwable? = null
    repeat(times) { attempt ->
        try {
            Log.i("VishalCCC", "retry: Attempt ${attempt + 1}")
            return block()
        } catch (e: Exception) {
            lastError = e
            delay(delayMillis)
        }
    }
    blockError.invoke()
    throw lastError ?: IllegalStateException("Unknown error")
}