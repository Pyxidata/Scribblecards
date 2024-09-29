package com.kevin1031.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevin1031.flashcards.data.Settings

@Entity(tableName = "savedSettings")
data class SavedSettings (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var masteryStandard: Int = Settings.DEFAULT_MASTERY_STANDARD,
    var priorityDeckMasteryLevel: Float = Settings.DEFAULT_PRIORITY_DECK_MASTERY_LEVEL,
    var timeImpactCoefficient: Float = Settings.DEFAULT_TIME_IMPACT_COEFFICIENT,
    var priorityDeckRefreshTime: Long = Settings.DEFAULT_PRIORITY_DECK_REFRESH_TIME,
    var isMasteryAffectedByTime: Boolean = Settings.DEFAULT_IS_MASTERY_AFFECTED_BY_TIME,
    var language: Language = Settings.DEFAULT_LANGUAGE,
)

enum class Language(val localeString: String, val displayName: String) {
    KOR(localeString = "ko", displayName = "한국어"),
    ENG(localeString = "en", displayName = "English"),
    JPN(localeString = "ja", displayName = "日本語"),
    UNSET(localeString = "en", displayName = "Default")
}