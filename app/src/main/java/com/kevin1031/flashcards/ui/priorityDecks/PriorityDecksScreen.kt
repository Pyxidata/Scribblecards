package com.kevin1031.flashcards.ui.priorityDecks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.ui.AppViewModelProvider
import kotlin.math.roundToInt

@Composable
fun PriorityDecksScreen(
    viewModel: PriorityDecksViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDeckButtonClicked: (Long) -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }
    val uiState by viewModel.uiState.collectAsState()
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)

    Scaffold(
        topBar = {
            TopAppBar(
                onBackButtonClicked = onBackButtonClicked,
            )
        },
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(mediumPadding)
            ) {
                Text(
                    text = if (uiState.decks == null) "" else "${uiState.decks?.size ?: 0}",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = if (uiState.decks?.size == 1) stringResource(id = R.string.pd_deck_remaining) else stringResource(id = R.string.pd_decks_remaining),
                    fontSize = 20.sp,
                )
            }

            if (!uiState.decks.isNullOrEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(smallPadding)
                ) {
                    items(uiState.decks!!.size) { i ->
                        DeckComponent(
                            onDeckOpened = { onDeckButtonClicked(uiState.decks!![i].id) },
                            deck = uiState.decks!![i]
                        )
                    }
                }

            } else if (uiState.decks != null && uiState.decks!!.isEmpty()) {
                Spacer(modifier = Modifier.height(largePadding*2))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(86.dp)
                )
                Text(
                    text = stringResource(id = R.string.pd_よしよし),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(mediumPadding)
                )

            }
        }
    }
}


@Composable
fun DeckComponent(
    onDeckOpened: (Long) -> Unit,
    deck: Deck,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    FilledTonalButton(
        onClick = { onDeckOpened(deck.id) },
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(smallPadding)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "${(deck.masteryLevel*100).roundToInt()}%",
                fontSize = 28.sp,
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = mediumPadding)
            )
            Text(
                text = deck.name,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onBackButtonClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.pd), overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.pd)
                )
            }
        }
    )
}