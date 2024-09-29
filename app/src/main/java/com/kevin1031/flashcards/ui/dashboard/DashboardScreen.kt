package com.kevin1031.flashcards.ui.dashboard

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.Constants
import com.kevin1031.flashcards.data.entities.Bundle
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.relations.BundleWithDecks
import com.kevin1031.flashcards.ui.AppViewModelProvider
import com.kevin1031.flashcards.ui.component.TextFieldDialog
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val BOX_SIZE_DP = 120
private const val BOX_SIZE_IN_BUNDLE_DP = 110

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDeckButtonClicked: (Long) -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val snackbarHostState = remember { SnackbarHostState() }
    var topAppBarHeight by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            if (uiState.isBundleCreatorOpen) {
                BundleCreatorTopAppBar(
                    numSelected = uiState.numSelectedDecks + uiState.numSelectedBundles,
                    onCloseClicked = { viewModel.closeBundleCreator() },
                    onCreateClicked = { viewModel.openBundleCreatorDialog() },
                    onMoveClicked = {
                        viewModel.openBundleSelector()
                        viewModel.setDeckEnabled(false)
                        viewModel.requestCloseBundleAnim()
                    },
                    setHeight = { topAppBarHeight = it },
                )

            } else if (uiState.isBundleSelectorOpen) {
                BundleSelectorTopAppBar(
                    numSelected = uiState.numSelectedBundles,
                    onCloseClicked = {
                        viewModel.backFromBundleSelector()
                        viewModel.setDeckEnabled(true)
                    },
                    onMoveClicked = {
                        coroutineScope.launch {
                            viewModel.moveSelectedDecksToSelectedBundle()
                            viewModel.setDeckEnabled(true)
                            viewModel.closeBundleSelector()
                        }
                    },
                    setHeight = { topAppBarHeight = it },
                    )
            } else if (uiState.isBundleOpen) {
                if (uiState.isRemoveDeckFromBundleUiOpen) {
                    RemoveDeckFromBundleTopBar(
                        numSelected = uiState.numSelectedDecks,
                        closeRemoveDeckFromBundleUi = { viewModel.closeRemoveDeckFromBundleUi() },
                        onRemoveClicked = {
                            coroutineScope.launch {
                                viewModel.moveSelectedDecksOutOfBundle()
                                viewModel.closeBundle()
                            }
                        },
                        setHeight = { topAppBarHeight = it },
                        )
                } else {
                    BundleTopAppBar(
                        onBackButtonClicked = { viewModel.requestCloseBundleAnim(); },
                        onEditBundleNameButtonClicked = { viewModel.openEditBundleNameDialog() },
                        onSortButtonClicked = { viewModel.cycleSortType() },
                        currentSortType = uiState.sortType,
                        title = viewModel.getBundle(uiState.currentBundleIndex!!)?.bundle?.name ?: "",
                        setHeight = { topAppBarHeight = it },
                    )
                }

            } else {
                DashboardTopAppBar(
                    onBackButtonClicked = onBackButtonClicked,
                    onSortButtonClicked = { viewModel.cycleSortType() },
                    currentSortType = uiState.sortType,
                    setHeight = { topAppBarHeight = it },
                )
            }
        },
        floatingActionButton = {
            val pfx = stringResource(id = R.string.ds_e_deck_limit)
            val sfx = stringResource(id = R.string.ds_e_deck_limit_sfx)
            if (!uiState.isBundleCreatorOpen) {
                CreateOptionButton(
                    isCreateOptionsOpen = uiState.isCreateOptionsOpen,
                    isCreateOptionsAnimRequested = uiState.isCreateOptionsCloseAnimRequested,
                    openBundleCreator = { viewModel.openBundleCreator() },
                    openDeckCreator = { viewModel.openDeckCreatorDialog() },
                    onTooManyDecks = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "$pfx${Constants.MAX_DECKS}$sfx",
                                withDismissAction = true,
                            )
                        }
                    },
                    getNumDecks = {
                        var n = uiState.decks.size
                        for (bundle in uiState.bundles) {
                            n += bundle.decks.size
                        }
                        n
                    },
                    toggleCreateOptions = { viewModel.toggleCreateOptions() },
                    requestCloseCreateOptions = { viewModel.requestCloseCreateOptionsAnim() },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->

        var containerSize by remember { mutableStateOf(IntSize.Zero) }
        var dragOffset by remember { mutableStateOf(Offset.Zero) }
        val bundleCloseAnim = animateFloatAsState(
            targetValue = if (uiState.isBundleCloseAnimRequested) 0f else if (uiState.isBundleOpen) 1f else 0f,
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutSlowInEasing,
            ),
            finishedListener = {
                if (uiState.isBundleCloseAnimRequested) {
                    viewModel.closeBundle()
                }
            }
        )

        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (uiState.isCreateOptionsOpen) viewModel.toggleCreateOptions()
                    }
                }
                .padding(innerPadding)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
        ) {

            val mediumPadding = dimensionResource(id = R.dimen.padding_medium)

            val lazyGridState = rememberLazyGridState()
            var lazyGridHeight by remember { mutableIntStateOf(0) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur((12 * bundleCloseAnim.value).dp)
                    .alpha(if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.R) 1f - bundleCloseAnim.value*0.7f else 1f)
            ) {

                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Adaptive(minSize = BOX_SIZE_DP.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(mediumPadding)
                        .onGloballyPositioned { lazyGridHeight = it.size.height }
                ) {

                    items(viewModel.getNumBundles()) { i ->
                        SelectableComposable(
                            index = i,
                            onBundleOpened = { viewModel.openBundle(it) },
                            onBundleSelected = {
                                if (uiState.isBundleSelectorOpen) {
                                    viewModel.deselectAllBundles()
                                    viewModel.toggleBundleSelection(i)
                                }
                            },
                            getBundle = { viewModel.getBundle(it) ?: BundleWithDecks(Bundle(), listOf()) },
                            isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                            isBundleSelectorOpen = uiState.isBundleSelectorOpen,
                            isBundle = true,
                            size = BOX_SIZE_DP,
                            isClickEnabled = !uiState.isDragging,
                        )
                    }

                    items(viewModel.getNumDecks()) { i ->
                        SelectableComposable(
                            index = i,
                            onDeckOpened = { onDeckButtonClicked(it) },
                            onDeckSelected = { viewModel.toggleDeckSelection(it) },
                            getDeck = { viewModel.getDeck(i) },
                            isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                            isRemoveDeckFromBundleUiOpen = uiState.isRemoveDeckFromBundleUiOpen,
                            isBundleSelectorOpen = uiState.isBundleSelectorOpen,
                            isBundle = false,
                            size = BOX_SIZE_DP,
                            onLongPress = { viewModel.openBundleCreator() },
                            isClickEnabled = !uiState.isDragging && uiState.isDeckEnabled,
                        )
                    }
                }
            }

            if (uiState.isBundleOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { viewModel.requestCloseBundleAnim() } }
                )

                val smallPadding = dimensionResource(R.dimen.padding_small)
                var bundleRect by remember { mutableStateOf(Rect.Zero) }

                var bundleContainerSize by remember { mutableStateOf(IntSize.Zero) }
                var bundleContainerPosition by remember { mutableStateOf(Offset.Zero) }

                val overlayWidth =
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                        (configuration.screenWidthDp - mediumPadding.value*2)*0.6f
                    else
                        configuration.screenWidthDp - mediumPadding.value*2
                val overlayHeight =
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                        configuration.screenHeightDp - mediumPadding.value*2
                    else
                        configuration.screenWidthDp - mediumPadding.value*2

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface
                    ),
                    modifier = Modifier
                        .alpha(bundleCloseAnim.value)
                        .padding(mediumPadding)
                        .size(overlayWidth.dp, overlayHeight.dp)
                        .onGloballyPositioned {
                            it
                                .boundsInWindow()
                                .let { rect ->
                                    bundleRect = rect
                                }
                        }

                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(smallPadding)
                            .onGloballyPositioned { coordinates ->
                                bundleContainerSize = coordinates.size
                                bundleContainerPosition = coordinates.positionInRoot()
                            }
                    ) {

                        val bundleLazyGridState = rememberLazyGridState()
                        var bundleLazyGridHeight by remember { mutableIntStateOf(0) }


                        LazyVerticalGrid(
                            state = bundleLazyGridState,
                            columns = GridCells.Adaptive(minSize = BOX_SIZE_IN_BUNDLE_DP.dp),
                            modifier = Modifier.onGloballyPositioned { bundleLazyGridHeight = it.size.height }
                        ) {

                            items(viewModel.getNumDecksInCurrentBundle()) { i ->
                                SelectableComposable(
                                    index = i,
                                    onDeckOpened = { onDeckButtonClicked(it) },
                                    onDeckSelected = {
                                        if (!uiState.isRemoveDeckFromBundleUiOpen && !uiState.isBundleCreatorOpen) {
                                            viewModel.openRemoveDeckFromBundleUi()
                                        }
                                        viewModel.toggleDeckSelection(it)
                                    },
                                    getDeck = { viewModel.getDeckFromCurrentBundle(it) ?: Deck() },
                                    getBundle = { viewModel.getBundle(it) ?: BundleWithDecks(Bundle(), listOf()) },
                                    isBundleCreatorOpen = uiState.isBundleCreatorOpen,
                                    isRemoveDeckFromBundleUiOpen = uiState.isRemoveDeckFromBundleUiOpen,
                                    isBundleSelectorOpen = uiState.isBundleSelectorOpen,
                                    isBundle = false,
                                    size = BOX_SIZE_IN_BUNDLE_DP,
                                    onLongPress = {
                                        if (!uiState.isBundleCreatorOpen) {
                                            viewModel.openRemoveDeckFromBundleUi()
                                        }
                                    },
                                    isClickEnabled = !uiState.isDragging && uiState.isDeckEnabled,
                                )
                            }
                        }
                    }
                }

                if (uiState.isBundleCloseAnimRequested) {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    if (uiState.isBundleCreatorDialogOpen) {
        TextFieldDialog(
            titleText = stringResource(id = R.string.ds_d_create_bundle),
            confirmText = stringResource(id = R.string.create),
            textFieldLabel = stringResource(id = R.string.ds_d_bundle_name),
            onDismissRequest = { viewModel.closeBundleCreatorDialog() },
            onConfirmClicked = {
                viewModel.closeBundleCreatorDialog()
                coroutineScope.launch {
                    viewModel.createBundle(it)
                    viewModel.closeBundleCreator()
                    viewModel.requestCloseBundleAnim()
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isDeckCreatorDialogOpen) {
        TextFieldDialog(
            titleText = stringResource(id = R.string.ds_d_create_deck),
            confirmText = stringResource(id = R.string.create),
            textFieldLabel = stringResource(id = R.string.ds_d_deck_name),
            onDismissRequest = { viewModel.closeDeckCreatorDialog() },
            onConfirmClicked = {
                viewModel.closeDeckCreatorDialog()
                viewModel.closeCreateOptions()
                coroutineScope.launch {
                    viewModel.createDeck(it)
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isEditBundleNameDialogOpen) {
        TextFieldDialog(
            titleText = stringResource(id = R.string.ds_d_create_bundle),
            confirmText = stringResource(id = R.string.save),
            textFieldLabel = stringResource(id = R.string.ds_d_bundle_name),
            onDismissRequest = { viewModel.closeEditBundleNameDialog() },
            onConfirmClicked = {
                viewModel.closeEditBundleNameDialog()
                coroutineScope.launch {
                    viewModel.updateCurrentBundleName(it)
                }
            },
            setUserInput = { viewModel.setUserInput(it) },
            userInput = uiState.userInput,
            focusManager = focusManager,
        )

    } else if (uiState.isCreateOptionsOpen) {
        BackHandler { viewModel.requestCloseCreateOptionsAnim() }

    } else if (uiState.isRemoveDeckFromBundleUiOpen && !uiState.isBundleCreatorOpen) {
        BackHandler { viewModel.closeRemoveDeckFromBundleUi()}

    } else if (uiState.isBundleOpen) {
        BackHandler { viewModel.requestCloseBundleAnim() }

    } else if (uiState.isBundleCreatorOpen) {
        BackHandler { viewModel.closeBundleCreator() }
    }
}

@Composable
fun SelectableComposable(
    index: Int,
    onDeckOpened: ((Long) -> Unit)? = null,
    onDeckSelected: ((Int) -> Unit)? = null,
    getDeck: ((Int) -> Deck)? = null,
    onBundleOpened: ((Int) -> Unit)? = null,
    onBundleSelected: ((Int) -> Unit)? = null,
    getBundle: ((Int) -> BundleWithDecks)? = null,
    isBundleCreatorOpen: Boolean,
    isRemoveDeckFromBundleUiOpen: Boolean = false,
    isBundleSelectorOpen: Boolean,
    isBundle: Boolean,
    size: Int,
    onLongPress: () -> Unit = {},
    onDraggedOver: () -> Unit = {},
    onDraggedAway: () -> Unit = {},
    dragPosition: Offset = Offset.Zero,
    isDropOnAllowed: Boolean = false,
    isClickEnabled: Boolean = true,
    alpha: Float = 1f,
) {

    var isVisible by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    var posInRoot by remember { mutableStateOf(Offset.Zero) }
    var isHighlighted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val content = @Composable {
        if (isBundle && onBundleOpened != null && onBundleSelected != null && getBundle != null) {
            BundleComponent(
                index = index,
                onBundleOpened = onBundleOpened,
                onBundleSelected = onBundleSelected,
                getBundle = getBundle,
                isHighlighted = isHighlighted,
                isClickEnabled = isClickEnabled && !isDragging,
                isBundleSelectorOpen = isBundleSelectorOpen,
            )
        } else if (onDeckOpened != null && onDeckSelected != null && getDeck != null) {
            DeckComponent(
                onDeckOpened = { onDeckOpened(it) },
                onDeckSelected = { onDeckSelected(index) },
                getDeck = { getDeck(index) },
                isBundleCreatorOpen = isBundleCreatorOpen,
                isHighlighted = isHighlighted,
                isClickEnabled = isClickEnabled && !isDragging,
                isRemoveDeckFromBundleUiOpen = isRemoveDeckFromBundleUiOpen,
                onLongPress = onLongPress,
            )
        } else Box {}
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .padding(dimensionResource(R.dimen.padding_small))
            .alpha(alpha * (if (isDragging) 0.5f else 1f))
            .zIndex(if (isDragging) 1f else 0f)
            .onGloballyPositioned {
                posInRoot = it.positionInRoot()
                it
                    .boundsInWindow()
                    .let { rect ->
                        if (isDropOnAllowed && !isDragging && rect.contains(dragPosition)) {
                            isHighlighted = true
                            onDraggedOver()
                        } else {
                            isHighlighted = false
                            onDraggedAway()
                        }
                    }
            }
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BundleComponent(
    index: Int,
    onBundleOpened: (Int) -> Unit,
    onBundleSelected: (Int) -> Unit,
    getBundle: (Int) -> BundleWithDecks,
    isHighlighted: Boolean,
    isClickEnabled: Boolean,
    isBundleSelectorOpen: Boolean,
) {

    val bundleWithDeck = getBundle(index)
    val bundle = bundleWithDeck.bundle

    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = if (bundle.isSelected || isHighlighted) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = if (bundle.isSelected || isHighlighted) MaterialTheme.colorScheme.onSecondary
            else MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                enabled = isClickEnabled,
                onClick = {
                    if (isBundleSelectorOpen) {
                        onBundleSelected(index)
                    } else {
                        onBundleOpened(index)
                    }
                },
            )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            var masteryLevel = 0f
            if (bundleWithDeck.decks.isNotEmpty()) {
                for (deck in bundleWithDeck.decks) {
                    masteryLevel += deck.masteryLevel
                }
                masteryLevel /= bundleWithDeck.decks.size
            }

            Text(
                text = bundle.name,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 3,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${(masteryLevel*100).roundToInt()}%",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeckComponent(
    onDeckOpened: (Long) -> Unit,
    onDeckSelected: () -> Unit,
    getDeck: () -> Deck,
    isBundleCreatorOpen: Boolean = false,
    isHighlighted: Boolean,
    isClickEnabled: Boolean = true,
    isRemoveDeckFromBundleUiOpen: Boolean = false,
    onLongPress: () -> Unit,
    ) {

    val deck = getDeck()

    Card(
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = if (deck.isSelected || isHighlighted) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (deck.isSelected || isHighlighted) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                enabled = isClickEnabled,
                onClick = {
                    if (isBundleCreatorOpen || isRemoveDeckFromBundleUiOpen) {
                        onDeckSelected()
                    } else {
                        onDeckOpened(deck.id)
                    }
                },
                onLongClick = {
                    if (!isBundleCreatorOpen && !isRemoveDeckFromBundleUiOpen) {
                        onLongPress()
                        onDeckSelected()
                    }
                }
            )

    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = deck.name,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 3,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            if (deck.isLocked) {
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    contentDescription = "Deck hidden",
                    modifier = Modifier.size(22.dp),
                )
            } else {
                Text(
                    text = "${(deck.masteryLevel * 100).roundToInt()}%",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(
    onBackButtonClicked: () -> Unit,
    onSortButtonClicked: () -> Unit,
    currentSortType: SortType,
    setHeight: (Int) -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.ds), overflow = TextOverflow.Ellipsis) },
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
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSortButtonClicked) {
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
                        },
                        fontSize = 12.sp,
                        modifier = Modifier.width(10.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .onGloballyPositioned {
                setHeight(it.size.height)
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleTopAppBar(
    onBackButtonClicked: () -> Unit,
    onEditBundleNameButtonClicked: () -> Unit,
    onSortButtonClicked: () -> Unit,
    currentSortType: SortType,
    title: String,
    setHeight: (Int) -> Unit,
    ) {
    TopAppBar(
        title = { Text(text = title, overflow = TextOverflow.Ellipsis,) },
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
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onEditBundleNameButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit bundle name"
                )
            }
            IconButton(onClick = onSortButtonClicked) {
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
                        },
                        fontSize = 12.sp,
                        modifier = Modifier.width(10.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .onGloballyPositioned {
                setHeight(it.size.height)
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCreatorTopAppBar(
    numSelected: Int,
    onCloseClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onMoveClicked: () -> Unit,
    setHeight: (Int) -> Unit,
    ) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    LargeTopAppBar(
        title = { Text(text = "$numSelected${stringResource(id = R.string.selected_)}") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        navigationIcon = {
            IconButton(
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = onMoveClicked,
                enabled = numSelected > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = stringResource(id = R.string.move),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Move",
                    )
                }
            }
            Button(
                onClick = onCreateClicked,
                enabled = numSelected > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = stringResource(id = R.string.create),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Default.CreateNewFolder,
                        contentDescription = "Create",
                    )
                }
            }
        },
        modifier = Modifier
            .onGloballyPositioned {
                setHeight(it.size.height)
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleSelectorTopAppBar(
    numSelected: Int,
    onCloseClicked: () -> Unit,
    onMoveClicked: () -> Unit,
    setHeight: (Int) -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    LargeTopAppBar(
        title = { Text(text = stringResource(id = R.string.ds_tb_select_bundle)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        navigationIcon = {
            IconButton(
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Button(
                onClick = onMoveClicked,
                enabled = numSelected > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = stringResource(id = R.string.move),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Move",
                    )
                }
            }
        },
        modifier = Modifier
            .onGloballyPositioned {
                setHeight(it.size.height)
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDeckFromBundleTopBar(
    numSelected: Int,
    closeRemoveDeckFromBundleUi: () -> Unit,
    onRemoveClicked: () -> Unit,
    setHeight: (Int) -> Unit,
) {

    val smallPadding = dimensionResource(R.dimen.padding_small)

    LargeTopAppBar(
        title = { Text(text = "$numSelected${stringResource(R.string.selected_)}") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        navigationIcon = {
            IconButton(
                onClick = { closeRemoveDeckFromBundleUi() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    onRemoveClicked()
                },
                enabled = numSelected > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .padding(smallPadding)
            ) {
                Row() {
                    Text(
                        text = stringResource(R.string.remove),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.size(smallPadding))
                    Icon(
                        imageVector = Icons.Filled.ArrowOutward,
                        contentDescription = "Remove",
                    )
                }
            }
        },
        modifier = Modifier
            .onGloballyPositioned {
                setHeight(it.size.height)
            }
    )
}

@Composable
fun CreateOptionButton(
    isCreateOptionsOpen: Boolean,
    isCreateOptionsAnimRequested: Boolean,
    openBundleCreator: () -> Unit,
    openDeckCreator: () -> Unit,
    onTooManyDecks: () -> Unit,
    getNumDecks: () -> Int,
    toggleCreateOptions: () -> Unit,
    requestCloseCreateOptions: () -> Unit,
    ) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val optionOpenAnim = animateFloatAsState(
        targetValue = if (isCreateOptionsAnimRequested) 0f else if (isCreateOptionsOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (isCreateOptionsAnimRequested) toggleCreateOptions()
        }
    )

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (isCreateOptionsOpen) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .offset(x = ((1f - optionOpenAnim.value) * 100).dp)
                    .alpha(optionOpenAnim.value)

            ) {
                FloatingActionButton(
                    onClick = {
                        if (getNumDecks() < Constants.MAX_DECKS) openDeckCreator()
                        else onTooManyDecks()
                    },
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = stringResource(id = R.string.ds_ab_create_deck), modifier = Modifier.padding(smallPadding))
                }
                FloatingActionButton(
                    onClick = openBundleCreator,
                    modifier = Modifier.padding(smallPadding)
                ) {
                    Text(text = stringResource(id = R.string.ds_ab_move_deck), modifier = Modifier.padding(smallPadding))
                }
            }
        }
        FloatingActionButton(
            onClick = {
                if (isCreateOptionsOpen) {
                    requestCloseCreateOptions()
                } else {
                    toggleCreateOptions()
                }
            },
            modifier = Modifier.padding(smallPadding)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Open",
                modifier = Modifier
                    .rotate(optionOpenAnim.value * -45f)
            )
        }
    }
}
