package com.kevin1031.flashcards.ui.mainMenu


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.entities.Language
import com.kevin1031.flashcards.ui.AppViewModelProvider
import com.kevin1031.flashcards.ui.component.ConfirmOrCancelDialog
import com.kevin1031.flashcards.ui.component.TextDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.softReset()
    }

    val uiState by viewModel.uiState.collectAsState()
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                onBackButtonClicked = onBackButtonClicked,
                onResetButtonClicked = { viewModel.toggleResetDialog() },
            )
        },
        bottomBar = {
            Column {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actions = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = mediumPadding)
                        ) {
                            Button(
                                enabled = uiState.changeMade,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateSettings(context, configuration)
                                    }
                                },
                                modifier = Modifier.height(40.dp)
                            ) { Text(stringResource(id = R.string.save_changes)) }
                        }
                    }
                )
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = stringResource(id = R.string.ss_mastery),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = mediumPadding, top = mediumPadding, bottom = smallPadding)
            )

            Card {
                Column {
                    val tip1 = stringResource(id = R.string.ss_mastery_target_tip)
                    val tip2 = stringResource(id = R.string.ss_mastery_time_tip)
                    val tip3 = stringResource(id = R.string.ss_mastery_rate_tip)

                    SettingsTextField(
                        label = stringResource(id = R.string.ss_mastery_target),
                        value = "${uiState.masteryStandard ?: ""}",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {
                            val n = it.toIntOrNull()
                            viewModel.setMasteryStandard(n?.coerceIn(1..10) ?: n)
                        },
                        onTipButtonClicked = {
                            viewModel.setTip(tip1)
                            viewModel.toggleTip()
                        },
                    )
                    Divider()
                    SettingsSwitch(
                        label = stringResource(id = R.string.ss_mastery_time),
                        checked = uiState.isMasteryAffectedByTime ?: false,
                        onCheckedChange = { viewModel.setIsMasteryAffectedByTime(it) },
                        onTipButtonClicked = {
                            viewModel.setTip(tip2)
                            viewModel.toggleTip()
                        },
                    )
                    Divider()
                    SettingsTextField(
                        enabled = uiState.isMasteryAffectedByTime ?: false,
                        label = stringResource(id = R.string.ss_mastery_rate),
                        unit = "%",
                        value =
                        if (uiState.timeImpactCoefficient == null) ""
                        else "${(uiState.timeImpactCoefficient!! * 100).toInt()}",
                        keyboardType = KeyboardType.Decimal,
                        onValueChange = {
                            val n = it.toIntOrNull()
                            viewModel.setTimeImpactCoefficient(if (n != null) n.coerceIn(1..200) / 100f else null)
                        },
                        onTipButtonClicked = {
                            viewModel.setTip(tip3)
                            viewModel.toggleTip()
                        },
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.ss_primary),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = mediumPadding, top = mediumPadding, bottom = smallPadding)
            )

            Card {
                Column {
                    val tip1 = stringResource(id = R.string.ss_primary_mastery_tip)
                    val tip2 = stringResource(id = R.string.ss_primary_cooldown_tip)

                    SettingsTextField(
                        label = stringResource(id = R.string.ss_primary_mastery),
                        unit = "%",
                        value =
                            if (uiState.priorityDeckMasteryLevel == null) ""
                            else "${(uiState.priorityDeckMasteryLevel!!*100).toInt()}",
                        keyboardType = KeyboardType.Decimal,
                        onValueChange = {
                            val n = it.toIntOrNull()
                            viewModel.setPriorityDeckMasteryLevel(if (n != null) n.coerceIn(1..99)/100f else null)
                        },
                        onTipButtonClicked = {
                            viewModel.setTip(tip1)
                            viewModel.toggleTip()
                        },
                    )
                    Divider()
                    SettingsTextField(
                        label = stringResource(id = R.string.ss_primary_cooldown),
                        unit = "hr" + if (uiState.priorityDeckRefreshTime == 3600000.toLong()) "" else "s",
                        value =
                            if (uiState.priorityDeckRefreshTime == null) ""
                            else "${(uiState.priorityDeckRefreshTime!!/3600000).toInt()}",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {
                            val n = it.toLongOrNull()
                            viewModel.setPriorityDeckRefreshTime(if (n != null) n.coerceIn(0.toLong()..24.toLong()) * 3600000 else null)
                        },
                        onTipButtonClicked = {
                            viewModel.setTip(tip2)
                            viewModel.toggleTip()
                        },
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.ss_language),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = mediumPadding, top = mediumPadding, bottom = smallPadding)
            )

            var dropdownMenuState by remember { mutableIntStateOf(0) }

            Card(
                modifier = Modifier
                    .combinedClickable(
                        onClick = { dropdownMenuState = 1 },
                    )
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = mediumPadding)
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        text = uiState.language?.displayName ?: "",
                        fontSize = 16.sp,
                    )
                    DropdownMenu(
                        expanded = dropdownMenuState == 1,
                        onDismissRequest = { dropdownMenuState = 0 },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "English") },
                            onClick = { viewModel.setLanguage(Language.ENG); dropdownMenuState = 0 },
                        )
//                        DropdownMenuItem(
//                            text = { Text(text = "日本語") },
//                            onClick = { viewModel.setLanguage(Language.JPN); dropdownMenuState = 0 },
//                        )
                        DropdownMenuItem(
                            text = { Text(text = "한국어") },
                            onClick = { viewModel.setLanguage(Language.KOR); dropdownMenuState = 0 },
                        )
                    }
                }
            }
        }
    }

    if (uiState.isTipOpen) {
        TextDialog(
            onDismissRequest = { viewModel.toggleTip() },
            text = uiState.tip,
        )

    } else if (uiState.isResetDialogOpen) {
        ConfirmOrCancelDialog(
            titleText = stringResource(id = R.string.ss_d_revert),
            onDismissRequest = { viewModel.toggleResetDialog() },
            onConfirmClicked = {
                coroutineScope.launch{
                    viewModel.revertChanges(context, configuration)
                    viewModel.toggleResetDialog()
                }
            },
        )
    }
}

@Composable
fun SettingsSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTipButtonClicked: () -> Unit,
    ) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = mediumPadding)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                overflow = TextOverflow.Visible,
                maxLines = 2,
                fontSize = 16.sp,
            )
            IconButton(onClick = onTipButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Tip"
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun SettingsTextField(
    enabled: Boolean = true,
    label: String,
    unit: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTipButtonClicked: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = mediumPadding)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                overflow = TextOverflow.Visible,
                maxLines = 2,
                fontSize = 16.sp,
            )
            IconButton(onClick = onTipButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Tip"
                )
            }
        }
        OutlinedTextField(
            enabled = enabled,
            value = value,
            maxLines = 1,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done,
            ),
            suffix = { Text(unit) },
            modifier = Modifier
                .width(100.dp)
                .padding(end = smallPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    onBackButtonClicked: () -> Unit,
    onResetButtonClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.ss), overflow = TextOverflow.Ellipsis) },
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
            IconButton(onClick = onResetButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Refresh,

                    contentDescription = "Reset"
                )
            }
        }
    )
}