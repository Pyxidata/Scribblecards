package com.kevin1031.flashcards

import android.app.Application
import com.kevin1031.flashcards.data.AppContainer
import com.kevin1031.flashcards.data.AppDataContainer

class FlashcardApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}