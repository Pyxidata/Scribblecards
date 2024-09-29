package com.kevin1031.flashcards.ui.priorityDecks

import com.kevin1031.flashcards.data.entities.Deck

data class PriorityDecksUiState(
    val decks: List<Deck>? = null,

    val lastUpdated: Long = 0,
)