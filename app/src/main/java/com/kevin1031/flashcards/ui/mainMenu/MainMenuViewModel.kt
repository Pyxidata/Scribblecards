package com.kevin1031.flashcards.ui.mainMenu

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.Settings
import com.kevin1031.flashcards.data.entities.SavedSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(MainMenuUiState())
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    fun softReset() {
        viewModelScope.launch {
            countProprityDecks()
        }
        closeCloseDialog()
        closeAllCardsBtnAnimRequest()
        closePriorityDecksBtnAnimRequest()
    }

    fun reset() {
        softReset()
    }

    suspend fun loadSettings(context: Context, configuration: Configuration) {
        val savedSettings = cardsRepository.getAllSavedSettings()
        if (savedSettings.isEmpty()) {
            cardsRepository.insertSavedSettings(SavedSettings())
        }
        Settings.update(cardsRepository.getAllSavedSettings().first(), context, configuration)
        reset()
    }

    /**
     * WARNING - expensive function
     */
    suspend fun countProprityDecks() {
        val decksWithCards = cardsRepository.getAllDecksWithCards()
        for (deck in decksWithCards) {
            deck.updateMasteryLevel()
            cardsRepository.updateDeck(deck.deck)
        }

        val priorityDecksWithCards = decksWithCards.filter {
            it.deck.masteryLevel <= Settings.priorityDeckMasteryLevel &&
            it.cards.isNotEmpty() &&
            System.currentTimeMillis() - it.deck.dateStudied > Settings.priorityDeckRefreshTime
        }.sortedBy { it.deck.masteryLevel }

        _uiState.update { currentState ->
            currentState.copy(
                numPriorityDecks = priorityDecksWithCards.size,
            )
        }
    }

    fun requestAllCardsBtnAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                allCardsBtnAnimRequested = true,
            )
        }
    }

    fun closeAllCardsBtnAnimRequest() {
        _uiState.update { currentState ->
            currentState.copy(
                allCardsBtnAnimRequested = false,
            )
        }
    }

    fun requestPriorityDecksBtnAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                priorityDecksBtnAnimRequested = true,
            )
        }
    }

    fun closePriorityDecksBtnAnimRequest() {
        _uiState.update { currentState ->
            currentState.copy(
                priorityDecksBtnAnimRequested = false,
            )
        }
    }

    fun openCloseDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isCloseDialogOpen = true,
            )
        }
    }
    fun closeCloseDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isCloseDialogOpen = false,
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