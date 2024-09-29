package com.kevin1031.flashcards.ui.priorityDecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.Settings
import com.kevin1031.flashcards.data.entities.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PriorityDecksViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(PriorityDecksUiState())
    val uiState: StateFlow<PriorityDecksUiState> = _uiState.asStateFlow()

    init {
        reset()
    }

    fun softReset() {
        viewModelScope.launch {
            loadPriorityDecks()
        }
    }

    fun reset() {
        softReset()

        _uiState.update { currentState ->
            currentState.copy(
                decks = null,
            )
        }
    }

    /**
     * WARNING - expensive function
     */
    suspend fun loadPriorityDecks() {
        val decksWithCards = cardsRepository.getAllDecksWithCards()
        for (deck in decksWithCards) {
            deck.updateMasteryLevel()
            cardsRepository.updateDeck(deck.deck)
        }

        val priorityDecksWithCards = decksWithCards.filter {
            !it.deck.isLocked &&
            it.deck.masteryLevel <= Settings.priorityDeckMasteryLevel &&
            it.cards.isNotEmpty() &&
            System.currentTimeMillis() - it.deck.dateStudied > Settings.priorityDeckRefreshTime
        }.sortedBy { it.deck.masteryLevel }

        val priorityDecks = mutableListOf<Deck>()
        for (deck in priorityDecksWithCards) {
            priorityDecks.add(deck.deck)
        }

        _uiState.update { currentState ->
            currentState.copy(
                decks = priorityDecks,
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