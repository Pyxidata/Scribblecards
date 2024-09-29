package com.kevin1031.flashcards.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kevin1031.flashcards.FlashcardApplication
import com.kevin1031.flashcards.ui.dashboard.DashboardViewModel
import com.kevin1031.flashcards.ui.deck.DeckViewModel
import com.kevin1031.flashcards.ui.createCard.CreateCardViewModel
import com.kevin1031.flashcards.ui.deck.ImportCardsViewModel
import com.kevin1031.flashcards.ui.editCard.EditCardViewModel
import com.kevin1031.flashcards.ui.mainMenu.MainMenuViewModel
import com.kevin1031.flashcards.ui.mainMenu.SettingsViewModel
import com.kevin1031.flashcards.ui.priorityDecks.PriorityDecksViewModel
import com.kevin1031.flashcards.ui.session.SessionViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DashboardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
            )
        }
        initializer {
            DeckViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            SessionViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            CreateCardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            EditCardViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            ImportCardsViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
                savedStateHandle = this.createSavedStateHandle(),
            )
        }
        initializer {
            PriorityDecksViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
            )
        }
        initializer {
            MainMenuViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
            )
        }
        initializer {
            SettingsViewModel(
                cardsRepository = flashcardApplication().container.cardsRepository,
            )
        }
    }
}

fun CreationExtras.flashcardApplication(): FlashcardApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlashcardApplication)
