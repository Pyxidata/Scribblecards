package com.kevin1031.flashcards.data

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.kevin1031.flashcards.data.entities.Language
import com.kevin1031.flashcards.data.entities.SavedSettings

class Settings {
    companion object {

        const val DEFAULT_MASTERY_STANDARD = 5
        const val DEFAULT_PRIORITY_DECK_MASTERY_LEVEL = 0.3f
        const val DEFAULT_TIME_IMPACT_COEFFICIENT = 1f
        const val DEFAULT_PRIORITY_DECK_REFRESH_TIME = 3600000.toLong()
        const val DEFAULT_IS_MASTERY_AFFECTED_BY_TIME = true
        val DEFAULT_LANGUAGE = Language.UNSET

        var masteryStandard: Int = DEFAULT_MASTERY_STANDARD
        var priorityDeckMasteryLevel: Float = DEFAULT_PRIORITY_DECK_MASTERY_LEVEL
        var timeImpactCoefficient: Float = DEFAULT_TIME_IMPACT_COEFFICIENT
        var priorityDeckRefreshTime: Long = DEFAULT_PRIORITY_DECK_REFRESH_TIME
        var isMasteryAffectedByTime: Boolean = DEFAULT_IS_MASTERY_AFFECTED_BY_TIME
        var language: Language = DEFAULT_LANGUAGE

        fun update(savedSettings: SavedSettings, context: Context, configuration: Configuration) {
            masteryStandard = savedSettings.masteryStandard
            priorityDeckMasteryLevel = savedSettings.priorityDeckMasteryLevel
            timeImpactCoefficient = savedSettings.timeImpactCoefficient
            priorityDeckRefreshTime = savedSettings.priorityDeckRefreshTime
            isMasteryAffectedByTime = savedSettings.isMasteryAffectedByTime

            language = if (savedSettings.language == Language.UNSET) {
                when (ConfigurationCompat.getLocales(configuration).get(0)?.language) {
                    "ja" -> Language.JPN
                    "ko" -> Language.KOR
                    else -> Language.ENG
                }
            } else {
                savedSettings.language
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(savedSettings.language.localeString)
            } else {
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(savedSettings.language.localeString)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }
    }
}