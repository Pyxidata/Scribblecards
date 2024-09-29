package com.kevin1031.flashcards.data

class Constants {
    companion object {
        const val MAX_DECKS = 100
        const val MAX_CARDS = 100
        const val MAX_FILE_SIZE: Long = 10000000
        const val MAX_STROKES = 200
    }
}

enum class StringLength(val maxLength: Int) {
    SHORT(50),
    LONG(300),
    VLONG(100000),
}
