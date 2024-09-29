package com.kevin1031.flashcards.data

import com.kevin1031.flashcards.data.entities.Bundle
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.entities.SavedSettings
import com.kevin1031.flashcards.data.relations.BundleWithDecks
import com.kevin1031.flashcards.data.relations.BundleWithDecksWithCards
import com.kevin1031.flashcards.data.relations.DeckWithCards

interface CardsRepository {

    suspend fun insertSavedSettings(savedSettings: SavedSettings): Long

    suspend fun insertBundle(bundle: Bundle): Long

    suspend fun insertDeck(deck: Deck): Long

    suspend fun insertDeckToBundle(deck: Deck, bundleId: Long): Long

    suspend fun insertCard(card: Card): Long

    suspend fun insertCardToDeck(card: Card, deckId: Long): Long

    suspend fun updateSavedSettings(savedSettings: SavedSettings)

    suspend fun updateBundle(bundle: Bundle)

    suspend fun updateDeck(deck: Deck)

    suspend fun updateAllDecks(vararg deck: Deck)

    suspend fun updateCard(card: Card)

    suspend fun deleteSavedSettings(savedSettings: SavedSettings)

    suspend fun deleteBundle(bundle: Bundle)

    suspend fun deleteDeck(deck: Deck)

    suspend fun deleteDeckWithCards(deckWithCards: DeckWithCards)

    suspend fun deleteCard(card: Card)

    suspend fun getSavedSettings(id: Long): SavedSettings

    suspend fun getAllSavedSettings(): List<SavedSettings>

    suspend fun getBundle(id: Long): Bundle

    suspend fun getAllBundles(): List<Bundle>

    suspend fun getBundleWithDecks(id: Long): BundleWithDecks

    suspend fun getAllBundlesWithDecks(): List<BundleWithDecks>

    suspend fun getBundleWithDecksWithCards(id: Long): BundleWithDecksWithCards

    suspend fun getAllBundlesWithDecksWithCards(): List<BundleWithDecksWithCards>

    suspend fun getDeck(id: Long): Deck

    suspend fun getAllDecks(): List<Deck>

    suspend fun getDeckNotInBundle(id: Long): Deck

    suspend fun getAllDecksNotInBundle(): List<Deck>

    suspend fun getDeckWithCards(id: Long): DeckWithCards

    suspend fun getAllDecksWithCards(): List<DeckWithCards>

    suspend fun getDeckNotInBundleWithCards(id: Long): DeckWithCards

    suspend fun getAllDecksWithCardsNotInBundle(): List<DeckWithCards>

    suspend fun getCard(id: Long): Card

    suspend fun getAllCards(): List<Card>
}
