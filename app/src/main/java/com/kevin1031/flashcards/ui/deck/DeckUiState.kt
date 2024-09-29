package com.kevin1031.flashcards.ui.deck

import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.relations.DeckWithCards

data class DeckUiState(
    val deck: DeckWithCards = DeckWithCards(Deck(), listOf()),
    val param: Long = -1,

    val numSelectedCards: Int = 0,
    val sortType: SortType = SortType.MASTERY,

    val isTipOpen: Boolean = false,
    val isDeckDeleted: Boolean = false,
    val isSessionOptionsOpen: Boolean = false,
    val isCardSelectorOpen: Boolean = false,

    val isDeleteCardDialogOpen: Boolean = false,
    val isDeleteDeckDialogOpen: Boolean = false,
    val isEditDeckNameDialogOpen: Boolean = false,

    val userInput: String? = null,
    val tipText: String = "",

    val lastUpdated: Long = 0,
)

enum class SortType {
    ALPHANUMERICAL,
    MASTERY,
    FAVORITE,
}