package com.kevin1031.flashcards.data.entities

abstract class Selectable {

    var isSelected: Boolean = false

    open fun toggleSelection() {
        isSelected = !isSelected
    }

    open fun select() {
        isSelected = true
    }

    open fun deselect() {
        isSelected = false
    }
}