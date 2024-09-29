package com.kevin1031.flashcards.ui.mainMenu

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.Settings
import com.kevin1031.flashcards.data.entities.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val cardsRepository: CardsRepository,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        reset()
    }

    fun softReset() {
        viewModelScope.launch {
            val savedSettings = cardsRepository.getAllSavedSettings().first()
            _uiState.update { currentState ->
                currentState.copy(
                    savedSettings = savedSettings,
                    masteryStandard = savedSettings.masteryStandard,
                    priorityDeckMasteryLevel = savedSettings.priorityDeckMasteryLevel,
                    timeImpactCoefficient = savedSettings.timeImpactCoefficient,
                    priorityDeckRefreshTime = savedSettings.priorityDeckRefreshTime,
                    isMasteryAffectedByTime = savedSettings.isMasteryAffectedByTime,
                    language = savedSettings.language,
                    tip = "",
                    isTipOpen = false,
                    isResetDialogOpen = false,
                    changeMade = false,
                )
            }
        }
    }

    fun reset() {
        softReset()
    }

    fun toggleTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = !currentState.isTipOpen
            )
        }
    }

    fun setTip(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tip = text,
            )
        }
    }

    fun setMasteryStandard(masteryStandard: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                masteryStandard = masteryStandard,
                changeMade = true
            )
        }
    }

    fun setPriorityDeckMasteryLevel(priorityDeckMasteryLevel: Float?) {
        _uiState.update { currentState ->
            currentState.copy(
                priorityDeckMasteryLevel = priorityDeckMasteryLevel,
                changeMade = true
            )
        }
    }

    fun setTimeImpactCoefficient(timeImpactCoefficient: Float?) {
        _uiState.update { currentState ->
            currentState.copy(
                timeImpactCoefficient = timeImpactCoefficient,
                changeMade = true
            )
        }
    }

    fun setPriorityDeckRefreshTime(priorityDeckRefreshTime: Long?) {
        _uiState.update { currentState ->
            currentState.copy(
                priorityDeckRefreshTime = priorityDeckRefreshTime,
                changeMade = true
            )
        }
    }

    fun setIsMasteryAffectedByTime(isMasteryAffectedByTime: Boolean?) {
        _uiState.update { currentState ->
            currentState.copy(
                isMasteryAffectedByTime = isMasteryAffectedByTime,
                changeMade = true
            )
        }
    }

    fun setLanguage(language: Language?) {
        _uiState.update { currentState ->
            currentState.copy(
                language = language,
                changeMade = true
            )
        }
    }

    fun toggleResetDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isResetDialogOpen = !currentState.isResetDialogOpen
            )
        }
    }

    suspend fun updateSettings(context: Context, configuration: Configuration) {
        val newSavedSettings = _uiState.value.savedSettings.copy(
            masteryStandard = _uiState.value.masteryStandard ?: Settings.DEFAULT_MASTERY_STANDARD,
            priorityDeckMasteryLevel = _uiState.value.priorityDeckMasteryLevel ?: Settings.DEFAULT_PRIORITY_DECK_MASTERY_LEVEL,
            timeImpactCoefficient = _uiState.value.timeImpactCoefficient ?: Settings.DEFAULT_TIME_IMPACT_COEFFICIENT,
            priorityDeckRefreshTime = _uiState.value.priorityDeckRefreshTime ?: Settings.DEFAULT_PRIORITY_DECK_REFRESH_TIME,
            isMasteryAffectedByTime = _uiState.value.isMasteryAffectedByTime ?: Settings.DEFAULT_IS_MASTERY_AFFECTED_BY_TIME,
            language = uiState.value.language ?: Settings.DEFAULT_LANGUAGE,
        )
        cardsRepository.updateSavedSettings(savedSettings = newSavedSettings)
        Settings.update(savedSettings = newSavedSettings, context = context, configuration = configuration)
        _uiState.update { currentState ->
            currentState.copy(
                changeMade = false
            )
        }
    }

    suspend fun revertChanges(context: Context, configuration: Configuration) {
        _uiState.update { currentState ->
            currentState.copy(
                masteryStandard = Settings.DEFAULT_MASTERY_STANDARD,
                priorityDeckMasteryLevel = Settings.DEFAULT_PRIORITY_DECK_MASTERY_LEVEL,
                timeImpactCoefficient = Settings.DEFAULT_TIME_IMPACT_COEFFICIENT,
                priorityDeckRefreshTime = Settings.DEFAULT_PRIORITY_DECK_REFRESH_TIME,
                isMasteryAffectedByTime = Settings.DEFAULT_IS_MASTERY_AFFECTED_BY_TIME,
                language = Settings.DEFAULT_LANGUAGE,
            )
        }
        updateSettings(context, configuration)
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}