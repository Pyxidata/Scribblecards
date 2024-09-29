package com.kevin1031.flashcards.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kevin1031.flashcards.ui.createCard.CreateCardScreen
import com.kevin1031.flashcards.ui.dashboard.DashboardScreen
import com.kevin1031.flashcards.ui.deck.DeckScreen
import com.kevin1031.flashcards.ui.editCard.EditCardScreen
import com.kevin1031.flashcards.ui.importCards.ImportCardsScreen
import com.kevin1031.flashcards.ui.mainMenu.MainMenuScreen
import com.kevin1031.flashcards.ui.mainMenu.SettingsScreen
import com.kevin1031.flashcards.ui.priorityDecks.PriorityDecksScreen
import com.kevin1031.flashcards.ui.session.SessionScreen

enum class FlashcardScreen() {
    MainMenu,
    PriorityDecks,
    Dashboard,
    Deck,
    CreateCard,
    EditCard,
    ImportCards,
    Session,
    Settings,
}

@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = FlashcardScreen.MainMenu.name,
        modifier = modifier
    ) {
        composable(route = FlashcardScreen.MainMenu.name) {
            BackHandler(true) {}
            MainMenuScreen(
                onAllCardsButtonClicked = { navController.navigate(FlashcardScreen.Dashboard.name) },
                onPriorityDecksButtonClicked = { navController.navigate(FlashcardScreen.PriorityDecks.name) },
                onSettingsButtonClicked = { navController.navigate(FlashcardScreen.Settings.name) },
            )
        }
        composable(route = FlashcardScreen.Settings.name) {
            SettingsScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.PriorityDecks.name) {
            BackHandler(true) { navController.navigate(FlashcardScreen.MainMenu.name) }
            PriorityDecksScreen(
                onDeckButtonClicked = { navController.navigate("${FlashcardScreen.Deck.name}/$it") },
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(route = FlashcardScreen.Dashboard.name) {
            BackHandler(true) { navController.navigate(FlashcardScreen.MainMenu.name) }
            DashboardScreen(
                onDeckButtonClicked = { navController.navigate("${FlashcardScreen.Deck.name}/$it") },
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.Deck.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            DeckScreen(
                onBackButtonClicked = { navController.navigateUp() },
                onStartButtonClicked = { navController.navigate("${FlashcardScreen.Session.name}/$it") },
                onCreateCardButtonClicked = { navController.navigate("${FlashcardScreen.CreateCard.name}/$it") },
                onEditCardButtonClicked = { navController.navigate("${FlashcardScreen.EditCard.name}/$it") },
                onImportCardsButtonClicked = { navController.navigate("${FlashcardScreen.ImportCards.name}/$it") },
            )
        }
        composable(
            route = "${FlashcardScreen.CreateCard.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            CreateCardScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.EditCard.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            EditCardScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.ImportCards.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            ImportCardsScreen(
                onBackButtonClicked = { navController.navigateUp() },
            )
        }
        composable(
            route = "${FlashcardScreen.Session.name}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            BackHandler(true) {}
            SessionScreen(
                onQuit = {
                    navController.popBackStack()
                }
            )
        }
    }
}
