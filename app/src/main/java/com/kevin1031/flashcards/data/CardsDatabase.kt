package com.kevin1031.flashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevin1031.flashcards.data.entities.Bundle
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.entities.DeckCardCrossRef
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.entities.SavedSettings

@Database(entities = [SavedSettings::class, Bundle::class, Deck::class, Card::class, DeckCardCrossRef::class], version = 10, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {

    abstract fun cardsDao(): CardsDao

    companion object {

        @Volatile
        private var Instance: CardsDatabase? = null

        fun getDatabase(context: Context): CardsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CardsDatabase::class.java, "cards_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}