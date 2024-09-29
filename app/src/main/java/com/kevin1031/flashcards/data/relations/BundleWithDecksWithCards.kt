package com.kevin1031.flashcards.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kevin1031.flashcards.data.entities.Bundle
import com.kevin1031.flashcards.data.entities.Deck

data class BundleWithDecksWithCards(
    @Embedded val bundle: Bundle,
    @Relation(
        //entity = Deck::class,
        parentColumn = "id",
        entityColumn = "bundleId",
    )
    //val decks: List<DeckWithCards>
    val decks: List<Deck>
) {
}