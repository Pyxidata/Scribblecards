package com.kevin1031.flashcards.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kevin1031.flashcards.R
import com.kevin1031.flashcards.data.StringLength

@Composable
fun AlertDialog(
    text: String,
    closeText: String = stringResource(id = R.string.close),
    onDismissRequest: () -> Unit,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)
    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

            ) {
                Text(
                    text = text,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(top = largePadding)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(closeText) }
                }
            }
        }
    }
}

@Composable
fun TextDialog(
    text: String,
    closeText: String = stringResource(id = R.string.close),
    onDismissRequest: () -> Unit,
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)
    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()
            ) {
                Text(text = text, textAlign = TextAlign.Start)
                Spacer(modifier = Modifier.height(largePadding))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.size(120.dp, 40.dp)
                ) {
                    Text(text = closeText)
                }
            }
        }
    }
}

@Composable
fun ConfirmOrCancelDialog(
    titleText: String,
    descriptionText: String? = null,
    confirmText: String = stringResource(id = R.string.confirm),
    cancelText: String = stringResource(id = R.string.cancel),
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)
    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127)))
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

            ) {
                Text(
                    text = titleText,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier =
                        if (descriptionText == null)
                            Modifier.padding(top = smallPadding, bottom = largePadding)
                        else Modifier
                )
                if (descriptionText != null) {
                    Text(
                        text = descriptionText,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = smallPadding, bottom = largePadding)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(cancelText) }
                    Button(
                        onClick = onConfirmClicked,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(confirmText) }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldDialog(
    titleText: String,
    textFieldLabel: String,
    confirmText: String = stringResource(id = R.string.confirm),
    cancelText: String = stringResource(id = R.string.cancel),
    errorText: String = stringResource(id = R.string.e_field_required),
    onDismissRequest: () -> Unit,
    onConfirmClicked: (String) -> Unit,
    setUserInput: (String) -> Unit,
    userInput: String?,
    focusManager: FocusManager,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 127))) {}
    Dialog(onDismissRequest = { onDismissRequest() }) {

        val smallPadding = dimensionResource(R.dimen.padding_small)
        val mediumPadding = dimensionResource(R.dimen.padding_medium)
        val largePadding = dimensionResource(R.dimen.padding_large)
        var isError by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        Card(
            modifier = Modifier
                .fillMaxWidth().verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(mediumPadding)
                    .fillMaxWidth()

            ) {
                Text(
                    text = titleText,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier.padding(top = smallPadding, bottom = largePadding)
                ) {
                    if (isError) {
                        Text(
                            text = errorText,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    OutlinedTextField(
                        value = userInput ?: "",
                        onValueChange = { setUserInput(if (it.length <= StringLength.SHORT.maxLength) it else it.substring(0..StringLength.SHORT.maxLength)) },
                        label = { Text(textFieldLabel) },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(
                            FocusDirection.Exit) }),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(cancelText) }
                    Button(
                        onClick = {
                            if (userInput.isNullOrBlank()) {
                                isError = true
                            } else {
                                onConfirmClicked(userInput)
                            }
                        },
                        modifier = Modifier.size(120.dp, 40.dp)
                    ) { Text(confirmText) }
                }
            }
        }
    }
}