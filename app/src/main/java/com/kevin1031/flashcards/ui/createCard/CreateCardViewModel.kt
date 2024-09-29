package com.kevin1031.flashcards.ui.createCard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.entities.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCardViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateCardUiState())
    val uiState: StateFlow<CreateCardUiState> = _uiState.asStateFlow()

    init {
        val param: Long = checkNotNull(savedStateHandle["id"])
        reset()
        viewModelScope.launch {
            val deck = cardsRepository.getDeck(id = param)
            _uiState.update { currentState ->
                currentState.copy(
                    param = param,
                    deck = deck,
                )
            }
        }
    }

    fun softReset() {
    }

    fun reset() {
        softReset()

        _uiState.update { currentState ->
            currentState.copy(
                questionTextInput = "",
                answerTextInput = "",
                hintTextInput = "",
                exampleTextInput = "",
            )
        }
    }

    suspend fun createCard() {
        val card = Card(
            questionText = _uiState.value.questionTextInput.trim(),
            answerText = _uiState.value.answerTextInput.trim(),
            hintText = _uiState.value.hintTextInput.trim(),
            exampleText = _uiState.value.exampleTextInput.trim(),
        )
        cardsRepository.insertCardToDeck(card, _uiState.value.deck.id)
    }

    fun setQuestionTextInput(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                questionTextInput = text,
            )
        }
    }

    fun setAnswerTextInput(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                answerTextInput = text,
            )
        }
    }

    fun setHintTextInput(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                hintTextInput = text,
            )
        }
    }

    fun setExampleTextInput(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                exampleTextInput = text,
            )
        }
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}