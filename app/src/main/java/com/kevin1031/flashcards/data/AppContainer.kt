package com.kevin1031.flashcards.data

import android.content.Context

interface AppContainer {
    val cardsRepository: CardsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val cardsRepository: CardsRepository by lazy {
        OfflineCardsRepository(CardsDatabase.getDatabase(context).cardsDao())
    }
}
