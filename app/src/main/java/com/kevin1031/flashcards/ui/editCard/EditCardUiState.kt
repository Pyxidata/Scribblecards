package com.kevin1031.flashcards.ui.editCard

import com.kevin1031.flashcards.data.entities.Card

data class EditCardUiState(
    val card: Card = Card(questionText = "", answerText = ""),
    val param: Long = -1,

    val questionTextInput: String = "",
    val answerTextInput: String = "",
    val hintTextInput: String = "",
    val exampleTextInput: String = "",

    val clearCardHistory: Boolean = false,

    val lastUpdated: Long = 0,
)