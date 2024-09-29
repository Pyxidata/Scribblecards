package com.kevin1031.flashcards.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bundles")
data class Bundle (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String = "Bundle",

    ): Selectable() {

    }