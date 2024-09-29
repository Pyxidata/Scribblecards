//package com.kevin1031.flashcards.data
//
//import com.kevin1031.flashcards.data.entities.Bundle
//import com.kevin1031.flashcards.data.entities.Card
//import com.kevin1031.flashcards.data.entities.Deck
//import java.util.Date
//
//object DataSource {
//    val d1 = System.currentTimeMillis()-86400000*5
//    val d2 = System.currentTimeMillis()-86400000*2
//    val d3 = System.currentTimeMillis()-500000*2
//
//    var decks = listOf(
//        Bundle(
//            name = "Animals",
//            decks = listOf(
//                Deck(
//                    name = "Desert", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Rattlesnake", answerText = "Reptile"),
//                        Card(questionText = "Shrew", answerText = "Mammal"),
//                        Card(questionText = "Vulture", answerText = "Reptile"),
//                        Card(questionText = "Gecko", answerText = "Reptile"),
//                        Card(questionText = "Desert Toad", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Ocean", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Eel", answerText = "Fish"),
//                        Card(questionText = "Sardine", answerText = "Fish"),
//                        Card(questionText = "Mackerel", answerText = "Fish"),
//                        Card(questionText = "Oyster", answerText = "Mollusc"),
//                        Card(questionText = "Mussel", answerText = "Mollusc"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                )
//            )
//        ),
//    )
//    var decks = listOf(
//        Deck(
//            name = "Addition and Subtraction", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 2", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 3", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 4", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 5", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 6", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            name = "Math 7", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f,
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//    )
//}