package com.kevin1031.flashcards.ui.session

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.relations.DeckWithCards

data class SessionUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),
    val param: Long = -1,

    val currentCardIndex: Int = 0,

    val isFlipped: Boolean = false,
    val isHintShown: Boolean = false,
    val isExampleShown: Boolean = false,
    val isAnswerSeen: Boolean = false,
    val isHistoryShown: Boolean = false,
    val isQuitDialogOpen: Boolean = false,
    val isRestartDialogOpen: Boolean = false,
    val isTipDialogOpen: Boolean = false,
    val isSessionCompleted: Boolean = false,
    val isSlideAnimRequested: Boolean = false,
    val flipContent: Boolean = false,

    val activeCards: List<Int> = listOf(),
    val usedCards: List<Int> = listOf(),
    val completedCards: List<Int> = listOf(),
    val cardHistory: Map<Int, CardHistory> = mapOf(),

    val oldMasteryLevel: Float = 0f,
    val newMasteryLevel: Float = 0f,
    val numPerfect: Int = 0,
    val isCorrect: Boolean = false,

    val strokes: List<List<Line>> = mutableListOf(),

    val lastUpdated: Long = 0,
)

class CardHistory {
    private val MAX_HISTORY_COUNT = 5
    private val history: ArrayDeque<Boolean> = ArrayDeque()

    fun getHistory(): List<Boolean> {
        return history.toList()
    }

    fun add(data: Boolean) {
        history.addLast(data)
        if (history.size > MAX_HISTORY_COUNT) {
            history.removeFirst()
        }
    }

    fun clearRecent() {
        history.removeLast()
    }

    fun isComplete(isDoubleDifficulty: Boolean): Boolean {
        return if (isDoubleDifficulty) {
            history.size >= 2 && history.last() && history[history.size-2]
        } else {
            !history.isEmpty() && history.last()
        }
    }

    fun isPerfect(): Boolean {
        val history = getHistory()
        return history.isNotEmpty() && !history.contains(false)
    }
}

data class Line(
    val start: Offset,
    val end: Offset,
    var color: Color = Color.Black,
    var width: Float = 5f,
)