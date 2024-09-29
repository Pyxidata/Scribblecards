package com.kevin1031.flashcards.ui.editCard

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevin1031.flashcards.ui.theme.FlashcardsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.StringLength
import com.kevin1031.flashcards.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen (
    viewModel: EditCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackButtonClicked: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

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
                        text = stringResource(id = R.string.ec),
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
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = mediumPadding)
                    ) {
                        TextButton(
                            onClick = onBackButtonClicked,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            modifier = Modifier.size(120.dp, 40.dp)
                        ) { Text(stringResource(id = R.string.cancel)) }
                        Button(
                            onClick = {
                                var isError = false
                                if (uiState.questionTextInput.isBlank()) {
                                    isError = true
                                }
                                if (uiState.answerTextInput.isBlank()) {
                                    isError = true
                                }
                                if (!isError) {
                                    coroutineScope.launch {
                                        if (uiState.clearCardHistory) {
                                            viewModel.clearCardHistory()
                                        }
                                        viewModel.updateCard()
                                        onBackButtonClicked()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                            modifier = Modifier.size(120.dp, 40.dp)
                        ) { Text(stringResource(id = R.string.save)) }
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(smallPadding)
            ) {
                Spacer(modifier = Modifier.height(mediumPadding))
                CustomTextField(
                    text = stringResource(id = R.string.ec_question),
                    value = uiState.questionTextInput,
                    onValueChange = { viewModel.setQuestionTextInput(it) },
                    label = stringResource(id = R.string.ec_question_text),
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = stringResource(id = R.string.ec_answer),
                    value = uiState.answerTextInput,
                    onValueChange = { viewModel.setAnswerTextInput(it) },
                    label = stringResource(id = R.string.ec_answer_text),
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = stringResource(id = R.string.ec_hint),
                    value = uiState.hintTextInput,
                    onValueChange = { viewModel.setHintTextInput(it) },
                    label = stringResource(id = R.string.ec_hint_text),
                    focusManager = focusManager,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                CustomTextField(
                    text = stringResource(id = R.string.ec_example),
                    value = uiState.exampleTextInput,
                    onValueChange = { viewModel.setExampleTextInput(it) },
                    label = stringResource(id = R.string.ec_example_text),
                    focusManager = focusManager,
                    isLast = true,
                    modifier = Modifier
                        .padding(vertical = smallPadding)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.ec_clear_history),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                    Checkbox(
                        checked = uiState.clearCardHistory,
                        onCheckedChange = { viewModel.setClearCardHistory(it) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 5,
    focusManager: FocusManager,
    isLast: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.e_field_required_sfx),
    ) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text + if (isError) errorMessage else "",
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(if (it.length <= StringLength.LONG.maxLength) it else it.substring(0..StringLength.LONG.maxLength)) },
            label = { Text(text = label) },
            isError = isError,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(imeAction = if (isLast) ImeAction.Done else ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(if (isLast) FocusDirection.Exit else FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=393dp,height=808dp"
    //device = "spec:width=650dp,height=900dp"
    //device = "spec:orientation=landscape,width=393dp,height=808dp"
)
@Composable
fun CreateCardScreenPreview() {
    FlashcardsTheme() {
        EditCardScreen(viewModel(), {})
    }
}