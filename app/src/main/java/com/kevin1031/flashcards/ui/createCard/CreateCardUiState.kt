package com.kevin1031.flashcards.ui.createCard

import com.kevin1031.flashcards.data.entities.Deck

data class CreateCardUiState(
    val deck: Deck = Deck(),
    val param: Long = -1,

    val questionTextInput: String = "",
    val answerTextInput: String = "",
    val hintTextInput: String = "",
    val exampleTextInput: String = "",

    val lastUpdated: Long = 0,
)