package com.kevin1031.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.pow
import com.kevin1031.flashcards.data.Settings

@Entity(tableName = "cards")
data class Card (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var deckId: Long = -1, //-1 if not in deck
    var questionText: String,
    var answerText: String,
    var hintText: String? = null,
    var exampleText: String? = null,
    var numStudied: Int = 0,
    var numPerfect: Int = 0,
    var isFavorite: Boolean = false,
) : Selectable() {

    companion object {
        fun calculateMasteryLevel(numStudied: Int, numPerfect: Int, isAffectedByTime: Boolean = Settings.isMasteryAffectedByTime, millisSinceStudied: Long = 0): Float {
            return if (numStudied == 0) {
                0f
            } else if (isAffectedByTime) {
                val adjustedNumStudied = numStudied.coerceAtMost(Settings.masteryStandard).toFloat()
                val adjustedNumPerfect = numPerfect.coerceAtMost(Settings.masteryStandard).toFloat() / (1 + (Math.E.toFloat() - 1) * Settings.timeImpactCoefficient)
                val n = (adjustedNumStudied / Settings.masteryStandard) / ((millisSinceStudied.toFloat()) / (3600000f * adjustedNumStudied).pow(adjustedNumPerfect + 1) + 1f)
                n
            } else {
                numPerfect.coerceAtMost(Settings.masteryStandard).toFloat() / Settings.masteryStandard
            }
        }
    }

    fun applySessionResults(isPerfect: Boolean) {
        if (isPerfect) {
            numPerfect = (++numPerfect).coerceAtMost(Settings.masteryStandard)
        } else if (numStudied >= Settings.masteryStandard) {
            numPerfect--
        }
        numStudied = (++numStudied).coerceAtMost(Settings.masteryStandard)
    }

    fun getMasteryLevel(isAffectedByTime: Boolean = true, millisSinceStudied: Long = 0): Float {
        numStudied = numStudied.coerceAtMost(Settings.masteryStandard)
        numPerfect = numPerfect.coerceAtMost(Settings.masteryStandard)
        return calculateMasteryLevel(numStudied.coerceAtMost(Settings.masteryStandard), numPerfect.coerceAtMost(Settings.masteryStandard), isAffectedByTime, millisSinceStudied)
    }

    fun clearHistory() {
        numStudied = 0
        numPerfect = 0
    }
}