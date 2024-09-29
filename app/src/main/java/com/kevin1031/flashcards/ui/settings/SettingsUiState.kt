package com.kevin1031.flashcards.ui.mainMenu

import com.kevin1031.flashcards.data.entities.Language
import com.kevin1031.flashcards.data.entities.SavedSettings

data class SettingsUiState(
    val savedSettings: SavedSettings = SavedSettings(),

    val masteryStandard: Int? = null,
    val priorityDeckMasteryLevel: Float? = null,
    val timeImpactCoefficient: Float? = null,
    val priorityDeckRefreshTime: Long? = null,
    val isMasteryAffectedByTime: Boolean? = null,
    val language: Language? = null,

    val tip: String = "",
    val changeMade: Boolean = false,

    val isTipOpen: Boolean = false,
    val isResetDialogOpen: Boolean = false,

    val lastUpdated: Long = 0,
)