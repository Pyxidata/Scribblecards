package com.kevin1031.flashcards.ui.deck

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.Constants
import com.kevin1031.flashcards.data.Settings
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.relations.DeckWithCards
import com.kevin1031.flashcards.ui.AppViewModelProvider
import com.kevin1031.flashcards.ui.component.ConfirmOrCancelDialog
import com.kevin1031.flashcards.ui.component.TextDialog
import com.kevin1031.flashcards.ui.component.TextFieldDialog
import com.kevin1031.flashcards.ui.theme.md_theme_light_onSurfaceVariant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen (
    viewModel: DeckViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
    onStartButtonClicked: (Long) -> Unit,
    onCreateCardButtonClicked: (Long) -> Unit,
    onEditCardButtonClicked: (Long) -> Unit,
    onImportCardsButtonClicked: (Long) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                title = {
                    Text(
                        text = uiState.deck.deck.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { coroutineScope.launch { viewModel.toggleLock() } },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.deck.deck.isLocked) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Hide deck"
                        )
                    }
                    IconButton(
                        onClick = { onImportCardsButtonClicked(uiState.param) },
                        modifier = Modifier.size(42.dp)
                        ) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = "Import cards"
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setUserInput(uiState.deck.deck.name)
                            viewModel.toggleEditDeckNameDialog()
                        },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit deck name"
                        )
                    }
                    IconButton(
                        onClick = { viewModel.toggleDeleteDeckDialog() },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete deck"
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        val bottomBarHeight = 64.dp

        val customCardEditorBar = @Composable {
            val errorPfx = stringResource(id = R.string.dk_e_card_limit)
            val errorSfx = stringResource(id = R.string.dk_e_card_limit_sfx)

            CardEditorBar(
                onAllCardsSelected = { viewModel.selectAllCardsInCurrentDeck() },
                onAllCardsDeselected = { viewModel.deselectAllCards() },
                onCardSelectorOpened = { viewModel.openCardSelector() },
                onCardSelectorClosed = { viewModel.closeCardSelector() },
                onSortButtonClicked = { viewModel.cycleCardSort() },
                currentSortType = uiState.sortType,
                numCards = viewModel.getNumCardsInCurrentDeck(),
                numSelectedCards = uiState.numSelectedCards,
                isCardSelectorOpen = uiState.isCardSelectorOpen,
                onCreateButtonClicked = { onCreateCardButtonClicked(uiState.deck.deck.id) },
                onTooManyCards = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "$errorPfx${Constants.MAX_CARDS}$errorSfx",
                            withDismissAction = true,
                        )
                    }
                },
                onCardDeleteButtonClicked = { viewModel.toggleDeleteCardDialog() },
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            val lazyColumnState = rememberLazyListState()
            val isFirstItemVisible by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex == 0 } }
            var hidden by remember { mutableStateOf(true) }

            if (!hidden && !isFirstItemVisible) {
                hidden = true
            } else if (hidden && isFirstItemVisible) {
                hidden = false
            }

            if (hidden) customCardEditorBar()

            LazyColumn(state = lazyColumnState) {
                item {
                    DeckStats(
                        deck = uiState.deck,
                        modifier = Modifier.height(300.dp)
                    )
                }
                if (!hidden) item { customCardEditorBar() }

                val numCards = viewModel.getNumCardsInCurrentDeck()
                item {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                }
                items(numCards) { i ->
                    CardComponent(
                        card = uiState.deck.cards[i],
                        onCardSelected = {
                            viewModel.toggleCardSelection(i)
                            viewModel.openCardSelector()
                        },
                        onFavoriteCardButtonClicked = {
                            coroutineScope.launch {
                                viewModel.toggleCardFavorite(i)
                            }
                        },
                        onEditCardButtonClicked = { onEditCardButtonClicked(it) },
                        lastStudied = System.currentTimeMillis() - uiState.deck.deck.dateStudied,
                        lastUpdated = uiState.lastUpdated,
                        isQnAFlipped = uiState.deck.deck.flipQnA,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(bottomBarHeight))
                }
            }
        }

        val startBottomBar = @Composable {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { onStartButtonClicked(uiState.deck.deck.id) },
                            enabled = uiState.deck.cards.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                            modifier = Modifier
                                .width(160.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.start),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                            )
                        }
                        IconButton(
                            onClick = { viewModel.toggleSessionOptions() },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = if (uiState.isSessionOptionsOpen)
                                    Icons.Default.KeyboardArrowDown
                                else Icons.Default.KeyboardArrowUp,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                modifier = Modifier.height(bottomBarHeight)
            )
        }

        if (!uiState.isSessionOptionsOpen) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                startBottomBar()
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxHeight()
        ) {

            val tip = stringResource(id = R.string.dk_d_tip)
            val bottomBarHeight = with (LocalDensity.current) {bottomBarHeight.toPx().toInt()}

            AnimatedVisibility(
                visible = uiState.isSessionOptionsOpen,
                enter = slideInVertically(initialOffsetY = { it - bottomBarHeight }),
                exit = slideOutVertically(targetOffsetY = { it - bottomBarHeight }),
            ) {
                Column (
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    startBottomBar()
                    SessionOptions(
                        deck = uiState.deck,
                        setShowHints = {
                            uiState.deck.deck.showHints = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setShowExamples = {
                            uiState.deck.deck.showExamples = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setFlipQnA = {
                            uiState.deck.deck.flipQnA = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        setDoubleDifficulty = {
                            uiState.deck.deck.doubleDifficulty = it
                            viewModel.update()
                            coroutineScope.launch { viewModel.updateDeck() }
                        },
                        onTipButtonClicked = {
                            viewModel.setTipText(tip)
                            viewModel.toggleTip()
                        },
                    )
                }
            }
        }
    }

    if (uiState.isTipOpen) {
        TextDialog(
            text = uiState.tipText,
            onDismissRequest = { viewModel.toggleTip() }
        )

    } else if (uiState.isDeleteCardDialogOpen) {
        ConfirmOrCancelDialog(
            titleText = stringResource(id = R.string.dk_d_delete_card),
            descriptionText = stringResource(id = R.string.dk_d_no_undo),
            confirmText = stringResource(R.string.delete),
            onDismissRequest = { viewModel.toggleDeleteCardDialog() },
            onConfirmClicked = {
                coroutineScope.launch {
                    viewModel.deleteSelectedCardsInCurrentDeck()
                    viewModel.toggleDeleteCardDialog()
                }
            },
        )

    } else if (uiState.isEditDeckNameDialogOpen) {
        TextFieldDialog(
            titleText = stringResource(R.string.ds_d_deck_name),
            textFieldLabel = stringResource(R.string.ds_d_deck_name),
            confirmText = stringResource(R.string.save),
            onDismissRequest = { viewModel.toggleEditDeckNameDialog() },
            onConfirmClicked = {
                coroutineScope.launch {
                    viewModel.updateDeckName(it)
                    viewModel.toggleEditDeckNameDialog()
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
            )

    } else if (uiState.isDeleteDeckDialogOpen) {
        ConfirmOrCancelDialog(
            titleText = stringResource(id = R.string.dk_d_delete_deck),
            descriptionText = stringResource(id = R.string.dk_d_no_undo),
            confirmText = stringResource(R.string.delete),
            onDismissRequest = { viewModel.toggleDeleteDeckDialog() },
            onConfirmClicked = {
                coroutineScope.launch {
                    viewModel.deleteDeck()
                    viewModel.toggleDeleteDeckDialog()
                    onBackButtonClicked()
                }
            },
        )

    } else if (uiState.isSessionOptionsOpen) {
        BackHandler { viewModel.toggleSessionOptions() }

    } else if (uiState.isCardSelectorOpen) {
        BackHandler { viewModel.closeCardSelector() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditorBar(
    onAllCardsSelected: () -> Unit,
    onAllCardsDeselected: () -> Unit,
    onCardSelectorOpened: () -> Unit,
    onCardSelectorClosed: () -> Unit,
    onSortButtonClicked: () -> Unit,
    currentSortType: SortType,
    numCards: Int,
    numSelectedCards: Int,
    isCardSelectorOpen: Boolean,
    onCreateButtonClicked: () -> Unit,
    onTooManyCards: () -> Unit,
    onCardDeleteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        title = {
            Text(
                text = if (isCardSelectorOpen)
                    "$numSelectedCards / $numCards${stringResource(id = R.string.selected_)}"
                    else if (numCards == 1) "$numCards ${stringResource(id = R.string.dk_card_total)}"
                    else "$numCards ${stringResource(id = R.string.dk_cards_total)}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (isCardSelectorOpen) {
                IconButton(
                    onClick = onCardSelectorClosed,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Quit selector"
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    if (numCards < Constants.MAX_CARDS) onCreateButtonClicked()
                    else onTooManyCards()
                },
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add card"
                )
            }
            if (isCardSelectorOpen) {
                IconButton(
                    onClick = onCardDeleteButtonClicked,
                    enabled = numCards > 0 && numSelectedCards > 0,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete card"
                    )
                }
            }
            IconButton(
                onClick = onSortButtonClicked,
                modifier = Modifier.size(42.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                    Text(
                        text = when (currentSortType) {
                            SortType.ALPHANUMERICAL -> "A"
                            SortType.MASTERY -> "%"
                            SortType.FAVORITE -> "★"
                        },
                        fontSize = 12.sp,
                        modifier = Modifier.width(10.dp)
                    )
                }
            }
            Checkbox(
                onCheckedChange = {
                    if (numCards == numSelectedCards) {
                        onAllCardsDeselected()
                    } else {
                        onCardSelectorOpened()
                        onAllCardsSelected()
                    }
                },
                colors = CheckboxDefaults.colors (
                    checkedColor = MaterialTheme.colorScheme.secondary
                ),
                checked = numCards > 0 && numCards == numSelectedCards,
                enabled = numCards > 0,
                modifier = Modifier
                    .size(52.dp)
                    .padding(end = smallPadding)
            )
        },
        modifier = modifier
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
fun CardComponent(
    card: Card,
    onCardSelected: () -> Unit,
    onFavoriteCardButtonClicked: () -> Unit,
    onEditCardButtonClicked: (Long) -> Unit,
    isQnAFlipped: Boolean,
    lastStudied: Long,
    lastUpdated: Long,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    var isAnswerBlurred by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(bottom = smallPadding, start = smallPadding, end = smallPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = mediumPadding, end = smallPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
                    .padding(vertical = smallPadding)
            ) {
                Text(
                    text = if (isQnAFlipped) card.answerText else card.questionText,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = if (isQnAFlipped) card.questionText else card.answerText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .blur(if (isAnswerBlurred) 8.dp else 0.dp)
                        .background(if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.R && isAnswerBlurred) md_theme_light_onSurfaceVariant else Color.Transparent)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { isAnswerBlurred = !isAnswerBlurred }
                            )
                        }
                )
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${Math.round(card.getMasteryLevel(millisSinceStudied = lastStudied)*100)}% (${card.numPerfect}/${Settings.masteryStandard})",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.width(smallPadding))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Checkbox(
                    onCheckedChange = { onCardSelected() },
                    checked = card.isSelected,
                    modifier = Modifier.size(36.dp)
                )
                IconButton(
                    onClick = onFavoriteCardButtonClicked,
                    modifier = Modifier.size(36.dp)
                ) {
                    if (card.isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = "Toggle favorite ($lastUpdated)"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Toggle favorite ($lastUpdated)"
                        )
                    }
                }
                IconButton(
                    onClick = { onEditCardButtonClicked(card.id) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit card"
                    )
                }
            }
        }
    }
}

@Composable
fun DeckStats(
    deck: DeckWithCards,
    modifier: Modifier = Modifier,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val circleSize = 164.dp

    val masteryLevel = deck.deck.masteryLevel
    val timeSinceStudied = System.currentTimeMillis() - deck.deck.dateStudied
    val days = timeSinceStudied/86400000
    val hours = timeSinceStudied/3600000%24
    val minutes = (timeSinceStudied/60000).coerceAtLeast(1)%60

    val displayedMasteryLevel = animateFloatAsState(
        targetValue = masteryLevel,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing,
        ),
    )

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(mediumPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(bottom = mediumPadding)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier
                    .height(circleSize)
                    .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        progress = displayedMasteryLevel.value,
                        modifier = Modifier.size(circleSize),
                        strokeWidth = 8.dp,
                        trackColor = MaterialTheme.colorScheme.background,
                    )
                    Box(modifier = Modifier
                        .size(circleSize)
                        .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "${Math.round(displayedMasteryLevel.value*100)}%",
                            fontSize = 58.sp,
                        )
                    }
                }
                Text(
                    text = stringResource(id = R.string.dk_mastered),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(bottom = smallPadding)
                        .weight(1f)
                )
            }
             if (deck.deck.isLocked) {
                 Text(
                     text = stringResource(id = R.string.dk_deck_hidden),
                     fontSize = 32.sp,
                     modifier = Modifier
                 )
                 Text(
                     text = " ",
                     fontSize = 16.sp,
                 )
            } else if (deck.deck.isNeverStudied()) {
                 Text(
                     text = stringResource(id = R.string.dk_new_deck),
                     fontSize = 32.sp,
                     modifier = Modifier
                 )
                 Text(
                     text = " ",
                     fontSize = 16.sp,
                 )
            } else if (days < 1) {
                Text(
                    text =
                        "${hours} ${stringResource(id = R.string.hour)}" + (if(hours == 1.toLong()) ", " else "${stringResource(id = R.string.plural_suffix)}, ") +
                        "${minutes} ${stringResource(id = R.string.minute)}" + (if(minutes == 1.toLong()) "" else "${stringResource(id = R.string.plural_suffix)}"),
                    fontSize = 32.sp,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(id = R.string.dk_since_last_studied),
                    fontSize = 16.sp,
                    modifier = Modifier
                )
            } else {
                Text(
                    text =
                        "${days} ${stringResource(id = R.string.day)}" + (if(days == 1.toLong()) ", " else "${stringResource(id = R.string.plural_suffix)}, ") +
                        "${hours} ${stringResource(id = R.string.hour)}" + (if(hours == 1.toLong()) "" else "${stringResource(id = R.string.plural_suffix)}"),
                    fontSize = 32.sp,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(id = R.string.dk_since_last_studied),
                    fontSize = 16.sp,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun SessionOptions(
    deck: DeckWithCards,
    setShowHints: (Boolean) -> Unit,
    setShowExamples: (Boolean) -> Unit,
    setFlipQnA: (Boolean) -> Unit,
    setDoubleDifficulty: (Boolean) -> Unit,
    onTipButtonClicked: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding)
            .verticalScroll(rememberScrollState())
    ) {
        CustomSwitch(
            label = stringResource(id = R.string.dk_show_hints),
            checked = deck.deck.showHints,
            onChecked = setShowHints,
            modifier = Modifier
                .padding(horizontal = smallPadding)
        )
        CustomSwitch(
            label = stringResource(id = R.string.dk_show_examples),
            checked = deck.deck.showExamples,
            onChecked = setShowExamples,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = stringResource(id = R.string.dk_flip_QnA),
            checked = deck.deck.flipQnA,
            onChecked = setFlipQnA,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
        CustomSwitch(
            label = stringResource(id = R.string.dk_double_difficulty),
            checked = deck.deck.doubleDifficulty,
            onChecked = setDoubleDifficulty,
            showTip = true,
            onTipButtonClicked = onTipButtonClicked,
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .padding(top = smallPadding)
        )
    }
}

@Composable
fun CustomSwitch(
    label: String,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChecked: (Boolean) -> Unit,
    showTip: Boolean = false,
    onTipButtonClicked: () -> Unit = {},
    ) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Switch(
            checked = checked,
            onCheckedChange = { onChecked(it) },
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(text = label, overflow = TextOverflow.Ellipsis)

        if (showTip) {
            IconButton(onClick = onTipButtonClicked) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Back"
                )
            }
        }
    }
}