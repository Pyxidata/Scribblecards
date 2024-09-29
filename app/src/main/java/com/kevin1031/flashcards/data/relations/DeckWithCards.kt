package com.kevin1031.flashcards.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.entities.Deck

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId",
        //associateBy = Junction(DeckCardCrossRef::class),
    )
    var cards: List<Card>,
) {

    init {
        updateValues()
    }

    fun sortByQuestion() {
        cards = cards.sortedBy { it.questionText }
    }

    fun sortByAnswer() {
        cards = cards.sortedBy { it.answerText }
    }

    fun sortByMastery() {
        cards = cards.sortedBy { it.getMasteryLevel(millisSinceStudied = System.currentTimeMillis() - deck.dateStudied) }
    }

    fun sortByFavorite() {
        cards = cards.sortedByDescending { it.isFavorite }
    }

    fun updateValues() {
        updateMasteryLevel()
        updateNumSelected()
    }

    fun updateMasteryLevel() {
        if (cards.isEmpty()) {
            deck.masteryLevel = 0f
        } else {
            var sum = 0f
            for (card in cards) {
                sum += card.getMasteryLevel(millisSinceStudied = System.currentTimeMillis() - deck.dateStudied)
            }
            deck.masteryLevel = sum / cards.size
        }
    }

    fun updateNumSelected() {
        if (cards.isEmpty()) {
            deck.numSelected = 0
        } else {
            var num = 0
            for (card in cards) {
                if (card.isSelected) num++
            }
            deck.numSelected = num
        }
    }
}