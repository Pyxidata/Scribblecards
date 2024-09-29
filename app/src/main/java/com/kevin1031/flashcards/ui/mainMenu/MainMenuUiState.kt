package com.kevin1031.flashcards.ui.mainMenu

data class MainMenuUiState(
    val numPriorityDecks: Int = 0,

    val isCloseDialogOpen: Boolean = false,

    val allCardsBtnAnimRequested: Boolean = false,
    val priorityDecksBtnAnimRequested: Boolean = false,

    val lastUpdated: Long = 0,
)