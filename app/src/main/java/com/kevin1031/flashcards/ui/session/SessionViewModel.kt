package com.kevin1031.flashcards.ui.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.Constants
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.relations.DeckWithCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        reset()

        viewModelScope.launch {
            startSession(checkNotNull(savedStateHandle["id"]))
        }
    }

    fun softReset() {
        _uiState.update { currentState ->
            currentState.copy(
                isFlipped = false,
                isHintShown = false,
                isExampleShown = false,
                isAnswerSeen = false,
            )
        }
    }

    fun reset() {
        softReset()

        _uiState.update { currentState ->
            currentState.copy(
                deck = DeckWithCards(Deck(), listOf(Card(questionText="",answerText=""))),
                isHistoryShown = false,
                isSessionCompleted = false,
                isQuitDialogOpen = false,
                isRestartDialogOpen = false,
                isTipDialogOpen = false,
                currentCardIndex = 0,
                activeCards = listOf(),
                usedCards = listOf(),
                completedCards = listOf(),
                cardHistory = mapOf(0 to CardHistory()),
                oldMasteryLevel = 0f,
                strokes = listOf(),
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

    fun showHint() {
        _uiState.update { currentState ->
            currentState.copy(
                isHintShown = true
            )
        }
    }

    fun showExample() {
        _uiState.update { currentState ->
            currentState.copy(
                isExampleShown = true
            )
        }
    }

    fun toggleInfo() {
        _uiState.update { currentState ->
            currentState.copy(
                isHistoryShown = !currentState.isHistoryShown
            )
        }
    }

    fun toggleQuitDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isQuitDialogOpen = !currentState.isQuitDialogOpen
            )
        }
    }

    fun toggleRestartDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isRestartDialogOpen = !currentState.isRestartDialogOpen
            )
        }
    }

    fun toggleTipDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipDialogOpen = !currentState.isTipDialogOpen
            )
        }
    }

    suspend fun applySessionData() {
        for (i in 0..<_uiState.value.deck.cards.size) {
            val card = _uiState.value.deck.cards[i]
            card.applySessionResults(_uiState.value.cardHistory[i]!!.isPerfect())
            card.deselect()
            cardsRepository.updateCard(card)
        }

        _uiState.value.deck.updateValues()
        _uiState.value.deck.deck.dateUpdated = System.currentTimeMillis()
        _uiState.value.deck.deck.dateStudied = System.currentTimeMillis()
        _uiState.value.deck.deck.deselect()
        cardsRepository.updateDeck(_uiState.value.deck.deck)
    }

    fun getNewMasteryLevel(): Float {
        val cards = _uiState.value.deck.cards
        var sum = 0f
        for (i in 0..<cards.size) {
            sum += Card.calculateMasteryLevel(
                numStudied = cards[i].numStudied + 1,
                numPerfect = cards[i].numPerfect + if (_uiState.value.cardHistory[i]!!.isPerfect()) 1 else 0,
                millisSinceStudied = 0,
            )
        }
        return sum / cards.size
    }

    fun flipCard() {
        _uiState.update { currentState ->
            currentState.copy(
                isFlipped = !currentState.isFlipped,
                isAnswerSeen = true,
            )
        }
    }

    fun setContentFlip(flipContent: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                flipContent = flipContent,
            )
        }
    }

    suspend fun startSession(param: Long) {
        val deck = cardsRepository.getDeckWithCards(param)
        deck.updateValues()

        if (deck.deck.showHints) showHint()
        if (deck.deck.showExamples) showExample()

        val activeCards = (0..<deck.cards.size).toMutableList()
        val usedCards = mutableListOf<Int>()
        val completedCards = mutableListOf<Int>()

        val cardHistory = mutableMapOf<Int, CardHistory>()
        for (i in activeCards) {
            cardHistory[i] = CardHistory()
        }

        activeCards.shuffle()
        val currentCardIndex = activeCards.removeFirst()

        _uiState.update { currentState ->
            currentState.copy(
                param = param,
                deck = deck,
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
                completedCards = completedCards,
                cardHistory = cardHistory,
                oldMasteryLevel = deck.deck.masteryLevel
            )
        }
    }

    fun endSession() {
        _uiState.update { currentState ->
            currentState.copy(
                isSessionCompleted = true,
                newMasteryLevel = getNewMasteryLevel(),
                numPerfect = getNumPerfect(),
            )
        }
    }

    fun setIsCorrect(isCorrect: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isCorrect = isCorrect,
            )
        }
    }

    fun getCurrentDeck(): DeckWithCards {
        return _uiState.value.deck
    }

    fun getCurrentCard(): Card {
        return getCurrentDeck().cards[_uiState.value.currentCardIndex]
    }

    fun getNumPerfect(): Int {
        var num = 0
        for (history in _uiState.value.cardHistory) {
            if (history.value.isPerfect()) {
                num++
            }
        }
        return num
    }

    fun skipCard() {
        var currentCardIndex = _uiState.value.currentCardIndex
        val activeCards = _uiState.value.activeCards.toMutableList()
        val usedCards = _uiState.value.usedCards.toMutableList()

        usedCards.add(currentCardIndex)

        // if active cards are empty, move used cards back to active cards and shuffle
        if (activeCards.isEmpty()) {
            for (i in usedCards) {
                activeCards.add(i)
            }
            activeCards.shuffle()
            usedCards.clear()
        }

        currentCardIndex = activeCards.removeFirst()

        _uiState.update { currentState ->
            currentState.copy(
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
            )
        }
        softReset()
    }

    fun nextCard(isCorrect: Boolean = _uiState.value.isCorrect) {
        val deck = getCurrentDeck()
        var currentCardIndex = _uiState.value.currentCardIndex
        val activeCards = _uiState.value.activeCards.toMutableList()
        val usedCards = _uiState.value.usedCards.toMutableList()
        val completedCards = _uiState.value.completedCards.toMutableList()
        val currentCardHistory = _uiState.value.cardHistory[currentCardIndex]!!

        currentCardHistory.add(isCorrect)

        // if current card is completed, move to completed cards
        if (currentCardHistory.isComplete(isDoubleDifficulty = deck.deck.doubleDifficulty)) {
            completedCards.add(currentCardIndex)

        // if current card is not completed, add to used cards
        } else {
            usedCards.add(currentCardIndex)
        }

        if (activeCards.isEmpty()) {
            // if there are no cards left, end session
            if (usedCards.isEmpty()) {
                completedCards.clear()
                endSession()

            // if active cards is empty but there are still cards left in used cards, move them back to active cards and shuffle, then get new card
            } else {
                for (i in usedCards) {
                    activeCards.add(i)
                }
                activeCards.shuffle()
                usedCards.clear()

                // ensure that same card will not appear consecutively, except when there is only one card left
                var newCardIndex: Int
                while (true) {
                    newCardIndex = activeCards.removeFirst()
                    if (newCardIndex == currentCardIndex && activeCards.size > 1) {
                        activeCards.add(newCardIndex)
                    } else break
                }
                currentCardIndex = newCardIndex
            }

        } else {
            currentCardIndex = activeCards.removeFirst()
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentCardIndex = currentCardIndex,
                activeCards = activeCards,
                usedCards = usedCards,
                completedCards = completedCards,
            )
        }
        softReset()
    }

    suspend fun toggleCardFavorite(card: Card) {
        val isSelected = card.isSelected
       card.isFavorite = !card.isFavorite
        card.deselect()
        cardsRepository.updateCard(card)
        card.isSelected = isSelected
        update()
        _uiState.update { currentState ->
            currentState.copy(
                deck = cardsRepository.getDeckWithCards(_uiState.value.param),
            )
        }
    }

    fun requestSlideAnim() {
        _uiState.update { currentState ->
            currentState.copy(
                isSlideAnimRequested = true
            )
        }
    }

    fun completeSlideAnimRequest() {
        _uiState.update { currentState ->
            currentState.copy(
                isSlideAnimRequested = false
            )
        }
    }

    fun addStroke(stroke: List<Line>) {
        _uiState.update { currentState ->
            val strokes = currentState.strokes.toMutableList()
            strokes.add(stroke)
            if (strokes.size > Constants.MAX_STROKES) {
                strokes.removeFirst()
            }
            currentState.copy(
                strokes = strokes
            )
        }
    }

    fun undoStroke() {
        _uiState.update { currentState ->
            val strokes = currentState.strokes.toMutableList()
            if (strokes.isNotEmpty()) strokes.removeLast()
            currentState.copy(
                strokes = strokes
            )
        }
    }

    fun clearStrokes() {
        _uiState.update { currentState ->
            currentState.copy(
                strokes = listOf()
            )
        }
    }
}