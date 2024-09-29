package com.kevin1031.flashcards.ui.mainMenu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AssignmentLate
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.ui.AppViewModelProvider
import com.kevin1031.flashcards.ui.component.ConfirmOrCancelDialog
import com.kevin1031.flashcards.ui.theme.FlashcardsTheme

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAllCardsButtonClicked: () -> Unit,
    onPriorityDecksButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
) {

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    LaunchedEffect(Unit) {
        viewModel.loadSettings(context, configuration)
        viewModel.softReset()
    }

    BackHandler {
        viewModel.openCloseDialog()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

        Spacer(Modifier.weight(0.5f))
        Text(
            text = "Scribblecards",
            fontSize = 42.sp,
            overflow = TextOverflow.Visible,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Spacer(Modifier.weight(0.2f))
        FilledTonalButton(
            onClick = { onAllCardsButtonClicked() },
            shape = RoundedCornerShape(percent = 3),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .size(300.dp)
                .padding(mediumPadding)
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Dashboard,
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(128.dp)
                )
                Text(
                    text = stringResource(id = R.string.mms_cards),
                    fontSize = 42.sp,
                    lineHeight = 48.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        FilledTonalButton(
            onClick = { onPriorityDecksButtonClicked() },
            shape = RoundedCornerShape(percent = 6),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.numPriorityDecks == 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.secondary,
                contentColor = if (uiState.numPriorityDecks == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
            ),
            modifier = Modifier
                .size(
                    width = 300.dp,
                    height = 200.dp
                )
                .padding(mediumPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.AssignmentLate,
                    contentDescription = "Priority decks",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.mms_priority),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.weight(0.5f))
                Text(
                    text = "${uiState.numPriorityDecks}" + stringResource(id = R.string.mms_priority_remaining),
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.weight(0.5f))
        IconButton(
            onClick = { onSettingsButtonClicked() },
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .padding(mediumPadding)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                modifier = Modifier
                    .size(48.dp)
            )
        }
        Spacer(Modifier.weight(0.2f))
    }

    if (uiState.isCloseDialogOpen) {
        ConfirmOrCancelDialog(
            titleText = stringResource(id = R.string.mms_d_close),
            confirmText = stringResource(id = R.string.quit),
            cancelText = stringResource(id = R.string.cancel),
            onDismissRequest = { viewModel.closeCloseDialog() },
            onConfirmClicked = {
                val activity = (context as? Activity)
                activity?.finish()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    FlashcardsTheme {
        MainMenuScreen(viewModel(), {}, {}, {})
    }
}