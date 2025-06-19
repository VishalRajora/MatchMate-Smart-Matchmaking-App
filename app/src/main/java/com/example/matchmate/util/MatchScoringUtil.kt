package com.example.matchmate.util

import com.example.matchmate.modelclass.MateMatchResult

object MatchScoringUtil {

    fun computeMatchScore(
        user: MateMatchResult,
        currentUserAge: Int,
        currentUserCity: String,
    ): Int {
        val targetAge = user.dob.age
        val targetCity = user.location.city

        val ageDiff = kotlin.math.abs(currentUserAge - targetAge)
        val ageScore = when {
            ageDiff == 0 -> 50
            ageDiff <= 2 -> 45
            ageDiff <= 5 -> 35
            ageDiff <= 10 -> 20
            else -> 5
        }

        val cityScore = if (targetCity.equals(currentUserCity, ignoreCase = true)) 50 else 0
        return ageScore + cityScore
    }
}
