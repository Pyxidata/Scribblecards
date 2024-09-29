package com.kevin1031.flashcards.ui.editCard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditCardViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(EditCardUiState())
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    init {
        val param: Long = checkNotNull(savedStateHandle["id"])

        viewModelScope.launch {
            val card = cardsRepository.getCard(id = param)
            _uiState.update { currentState ->
                currentState.copy(
                    param = param,
                    card = card,
                    questionTextInput = card.questionText,
                    answerTextInput = card.answerText,
                    hintTextInput = card.hintText ?: "",
                    exampleTextInput = card.exampleText ?: "",
                    clearCardHistory = false,
                )
            }
        }
    }

    fun clearCardHistory() {
        _uiState.value.card.clearHistory()
    }

    suspend fun updateCard() {
        _uiState.value.card.questionText = _uiState.value.questionTextInput.trim()
        _uiState.value.card.answerText = _uiState.value.answerTextInput.trim()
        _uiState.value.card.hintText = _uiState.value.hintTextInput.trim()
        _uiState.value.card.exampleText = _uiState.value.exampleTextInput.trim()
        _uiState.value.card.deselect()
        cardsRepository.updateCard(_uiState.value.card)
    }

    fun setClearCardHistory(clearCardHistory: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                clearCardHistory = clearCardHistory,
            )
        }
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