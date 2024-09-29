package com.kevin1031.flashcards.ui.deck

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin1031.flashcards.data.CardsRepository
import com.kevin1031.flashcards.data.Constants
import com.kevin1031.flashcards.data.entities.Card
import com.kevin1031.flashcards.data.entities.Deck
import com.kevin1031.flashcards.data.relations.BundleWithDecks
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class ImportCardsViewModel(
    private val cardsRepository: CardsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(ImportCardsUiState())
    val uiState: StateFlow<ImportCardsUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                param = checkNotNull(savedStateHandle["id"])
            )
        }
        reset()
    }

    fun softReset() {
    }

    fun reset() {
        softReset()
        resetBringFromDecksScreen()
        resetImportThroughTextScreen()
        resetUploadCsvFileScreen()

        viewModelScope.launch {
            val deck = cardsRepository.getDeckWithCards(id = _uiState.value.param)
            val decks = cardsRepository.getAllDecks()
            val bundles = cardsRepository.getAllBundlesWithDecks()
            _uiState.update { currentState ->
                currentState.copy(
                    deck = deck,
                    decks = decks,
                    bundles = bundles,
                    subDecks = listOf(),
                    isBringFromDecksScreenOpen = false,
                    isImportThroughTextScreenOpen = false,
                    isUploadCsvFileScreenOpen = false,
                    isTipOpen = false,
                    isNoCardErrorDialogOpen = false,
                    numSelectedCards = 0,
                )
            }
        }
    }

    fun resetBringFromDecksScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedBundle = null,
                bFD_selectedDeck = null,
                bFD_maxMasteryLevel = 1f,
                bFD_excludeMastered = false,
                bFD_resetHistory = true,
            )
        }
    }

    fun resetImportThroughTextScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_errorState = ITT_ErrorState.NO_ERROR,
                iTT_errorState2 = ITT_ErrorState.NO_ERROR,
                iTT_inputText = "",
                iTT_questionLines = "",
                iTT_answerLines = "",
                iTT_hintLines = "",
                iTT_exampleLines = "",
                iTT_ignoredLines = "",
                iTT_focusRequested = false,
                iTT_focusRequesterT = FocusRequester(),
                iTT_focusRequesterQ = FocusRequester(),
                iTT_focusRequesterA = FocusRequester(),
                iTT_focusRequesterH = FocusRequester(),
                iTT_focusRequesterE = FocusRequester(),
                iTT_focusRequesterI = FocusRequester(),
                iTT_previewCard1 = null,
                iTT_previewCard2 = null,
            )
        }
    }

    fun resetUploadCsvFileScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_csvFileName = "",
                uCF_csvFileSize = 0,
                uCF_csvFileData = listOf(),
                uCF_errorState = UCF_ErrorState.NO_ERROR,
                uCF_questionIndex = 1,
                uCF_answerIndex = 2,
                uCF_hintIndex = null,
                uCF_exampleIndex = null,
                uCF_focusRequested = false,
                uCF_focusRequesterF = FocusRequester(),
            )
        }
    }

    fun getTotalNumCards(): Int {
        var n = 0
        for (subDeck in _uiState.value.subDecks) {
            n += subDeck.cards.size
        }
        return n
    }

    fun setInputText(inputText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_inputText = inputText
            )
        }
    }

    fun setQuestionLines(questionLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_questionLines = questionLines
            )
        }
    }

    fun setAnswerLines(answerLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_answerLines = answerLines
            )
        }
    }

    fun setHintLines(hintLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_hintLines = hintLines
            )
        }
    }

    fun setExampleLines(exampleLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_exampleLines = exampleLines
            )
        }
    }

    fun setIgnoredLines(ignoredLines: String) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_ignoredLines = ignoredLines
            )
        }
    }

    fun setQuestionIndex(questionIndex: String) {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_questionIndex = questionIndex.toIntOrNull()
            )
        }
    }

    fun setAnswerIndex(answerIndex: String) {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_answerIndex = answerIndex.toIntOrNull()
            )
        }
    }

    fun setHintIndex(hintIndex: String) {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_hintIndex = hintIndex.toIntOrNull()
            )
        }
    }

    fun setExampleIndex(exampleIndex: String) {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_exampleIndex = exampleIndex.toIntOrNull()
            )
        }
    }

    fun setMaxMasteryLevel(maxMasteryLevel: Float?) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_maxMasteryLevel = maxMasteryLevel
            )
        }
    }

    fun setExcludeMastered(excludeMastered: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_excludeMastered = excludeMastered
            )
        }
    }

    fun setResetHistory(resetHistory: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_resetHistory = resetHistory
            )
        }
    }

    fun toggleNoCardErrorDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isNoCardErrorDialogOpen = !_uiState.value.isNoCardErrorDialogOpen
            )
        }
    }

    fun toggleBringFromDecksScreen() {
        resetBringFromDecksScreen()
        _uiState.update { currentState ->
            currentState.copy(
                isBringFromDecksScreenOpen = !_uiState.value.isBringFromDecksScreenOpen
            )
        }
    }

    fun toggleImportThroughTextScreen() {
        resetImportThroughTextScreen()
        _uiState.update { currentState ->
            currentState.copy(
                isImportThroughTextScreenOpen = !_uiState.value.isImportThroughTextScreenOpen,
            )
        }
    }

    fun toggleUploadCsvFileScreen() {
        resetUploadCsvFileScreen()
        _uiState.update { currentState ->
            currentState.copy(
                isUploadCsvFileScreenOpen = !_uiState.value.isUploadCsvFileScreenOpen
            )
        }
    }

    fun toggleTip() {
        _uiState.update { currentState ->
            currentState.copy(
                isTipOpen = !_uiState.value.isTipOpen
            )
        }
    }

    fun addSubDeck(subDeck: SubDeck) {
        val subDecks = _uiState.value.subDecks.toMutableList()
        subDecks.add(subDeck)
        _uiState.update { currentState ->
            currentState.copy(
                subDecks = subDecks
            )
        }
    }

    fun removeSubDeck(subDeck: SubDeck) {
        val subDecks = _uiState.value.subDecks.toMutableList()
        subDecks.remove(subDeck)
        _uiState.update { currentState ->
            currentState.copy(
                subDecks = subDecks
            )
        }
    }


    fun selectBundle(bundle: BundleWithDecks?) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedBundle = bundle
            )
        }
    }

    fun selectDeck(deck: Deck?) {
        _uiState.update { currentState ->
            currentState.copy(
                bFD_selectedDeck = deck
            )
        }
    }

    fun setImportThroughScreenFocusRequest(requested: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                iTT_focusRequested = requested
            )
        }
    }

    fun setUploadCsvFileScreenFocusRequest(requested: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                uCF_focusRequested = requested
            )
        }
    }


    suspend fun importAll() {
        for (subDeck in _uiState.value.subDecks) {
            for (card in subDeck.cards) {
                cardsRepository.insertCardToDeck(card, _uiState.value.deck.deck.id)
            }
        }
    }

    suspend fun getAllCardsFromDeck(
        deck: Deck,
    ): List<Card>? {
        val cards = cardsRepository.getDeckWithCards(deck.id).cards
        val subDeckCards = mutableListOf<Card>()

        for (card in cards) {
            if (!_uiState.value.bFD_excludeMastered || card.getMasteryLevel() < (_uiState.value.bFD_maxMasteryLevel ?: 0f)) {
                subDeckCards.add(
                    if (_uiState.value.bFD_resetHistory)
                        Card(
                            questionText = card.questionText,
                            answerText = card.answerText,
                            hintText = card.hintText,
                            exampleText = card.exampleText,
                        )
                    else
                        Card(
                            questionText = card.questionText,
                            answerText = card.answerText,
                            hintText = card.hintText,
                            exampleText = card.exampleText,
                            numStudied = card.numStudied,
                            numPerfect = card.numPerfect,
                        )
                )
            }
        }

        if (subDeckCards.isEmpty()) {
            toggleNoCardErrorDialog()
            return null
        }

        return subDeckCards
    }

    fun textToCards(maxCards: Int = -1, checkForErrors: Boolean = false): List<Card>? {
        val qL = getParsedInputLines(_uiState.value.iTT_questionLines)
        val aL = getParsedInputLines(_uiState.value.iTT_answerLines)
        val hL = getParsedInputLines(_uiState.value.iTT_hintLines)
        val eL = getParsedInputLines(_uiState.value.iTT_exampleLines)
        val iL = getParsedInputLines(_uiState.value.iTT_ignoredLines)

        val QUESTION = 1
        val ANSWER = 2
        val HINT = 3
        val EXAMPLE = 4
        val IGNORED = 5

        val lineMap = mutableMapOf<Int, Int>()
        var max = 0
        val testError: (Int, Int) -> Boolean = { n, curr ->
            val isError = when (lineMap[n]) {
                curr -> false
                QUESTION -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.QUESTION_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                ANSWER -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.ANSWER_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                HINT -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.HINT_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                EXAMPLE -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.EXAMPLE_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                IGNORED -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.IGNORED_LINES_DUPLICATE,
                        )
                    }
                    true
                }
                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            iTT_errorState2 = ITT_ErrorState.NO_ERROR,
                        )
                    }
                    false
                }
            }

            isError
        }

        for (n in qL) {
            if (checkForErrors && testError(n, QUESTION)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.QUESTION_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = QUESTION
            if (n > max) max = n
        }
        for (n in aL) {
            if (checkForErrors && testError(n, ANSWER)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.ANSWER_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = ANSWER
            if (n > max) max = n
        }
        for (n in hL) {
            if (checkForErrors && testError(n, HINT)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.HINT_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = HINT
            if (n > max) max = n
        }
        for (n in eL) {
            if (checkForErrors && testError(n, EXAMPLE)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.EXAMPLE_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = EXAMPLE
            if (n > max) max = n
        }
        for (n in iL) {
            if (checkForErrors && testError(n, IGNORED)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        iTT_errorState = ITT_ErrorState.IGNORED_LINES_DUPLICATE
                    )
                }
                return null
            }
            lineMap[n] = IGNORED
            if (n > max) max = n
        }

        val subDeckCards = mutableListOf<Card>()
        var i = 0
        var qT = ""
        var aT = ""
        var hT = ""
        var eT = ""

        val temp = _uiState.value.iTT_inputText.split("\n")
        var numCards = 0
        for (segment in temp) {
            if (segment.isNotBlank()) {
                when (lineMap[i+1]) {
                    QUESTION -> qT += (if (qT.isBlank()) "" else "\n") + segment
                    ANSWER -> aT += (if (aT.isBlank()) "" else "\n") + segment
                    HINT -> hT += (if (hT.isBlank()) "" else "\n") + segment
                    EXAMPLE -> eT += (if (eT.isBlank()) "" else "\n") + segment
                    else -> {}
                }
                i++

                if (i == max && qT.isNotBlank() && aT.isNotBlank()) {
                    subDeckCards.add(
                        Card(
                            questionText = qT,
                            answerText = aT,
                            hintText = hT,
                            exampleText = eT,
                        )
                    )
                    i = 0
                    qT = ""
                    aT = ""
                    hT = ""
                    eT = ""
                    numCards++
                    if (checkForErrors && numCards > Constants.MAX_CARDS) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                iTT_errorState = ITT_ErrorState.TEXT_TOO_LONG
                            )
                        }
                        return null
                    } else if (checkForErrors && subDeckCards.isEmpty()) {
                        toggleNoCardErrorDialog()
                        return null
                    } else if (numCards == maxCards) {
                        return subDeckCards
                    }
                }
            }
        }

        if (i > 0 && checkForErrors) {
            _uiState.update { currentState ->
                currentState.copy(
                    iTT_errorState = ITT_ErrorState.TEXT_INCOMPLETE
                )
            }
            return null
        } else if (checkForErrors && subDeckCards.isEmpty()) {
            toggleNoCardErrorDialog()
            return null
        }

        _uiState.update { currentState ->
            currentState.copy(
                iTT_errorState = ITT_ErrorState.NO_ERROR
            )
        }
        return subDeckCards
    }

    private fun getParsedInputLines(inputLines: String): Set<Int> {
        val parsedLines = mutableSetOf<Int>()
        inputLines.trim()
        val lines = inputLines.split(",").toTypedArray()
        for (line in lines) {
            if (line.contains("-")) {
                val range = inputLines.split("-").toTypedArray()
                val start: Int? = range[0].toIntOrNull()
                val end: Int? = range[1].toIntOrNull()
                if (start != null && end != null) {
                    for (i in start..end) {
                        parsedLines.add(i)
                    }
                }
            }
            val parsedLine: Int? = line.toIntOrNull()
            if (parsedLine != null) {
                parsedLines.add(parsedLine)
            }
        }
        return parsedLines
    }

    fun updateImportThroughTextScreenPreviewCards() {
        val cards = textToCards(maxCards = 2)
        _uiState.update { currentState ->
            currentState.copy(
                iTT_previewCard1 = cards?.getOrNull(0),
                iTT_previewCard2 = cards?.getOrNull(1),
            )
        }
    }

    fun csvToStrList(context: Context, uri: Uri) {

        var fileName = ""
        var fileSize: Long = 0
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
            fileSize = cursor.getLong(sizeIndex)
            cursor.close()
        }

        if (!fileName.endsWith(".csv")) {
            _uiState.update { currentState ->
                currentState.copy(
                    uCF_csvFileName = fileName,
                    uCF_csvFileSize = fileSize,
                    uCF_csvFileData = listOf(),
                    uCF_errorState = UCF_ErrorState.INVALID_FILE_FORMAT
                )
            }
            return

        } else if (fileSize > Constants.MAX_FILE_SIZE) {
            _uiState.update { currentState ->
                currentState.copy(
                    uCF_csvFileName = fileName,
                    uCF_csvFileSize = fileSize,
                    uCF_csvFileData = listOf(),
                    uCF_errorState = UCF_ErrorState.FILE_TOO_LARGE
                )
            }
            return
        }

        val csvReader = CSVReader(InputStreamReader(context.contentResolver.openInputStream(uri)))
        val data = csvReader.readAll()
        val strList = mutableListOf<List<String>>()
        for (rowData in data) {
            val row = mutableListOf<String>()
            for (item in rowData) {
                row.add(item)
            }
            strList.add(row)
        }

        _uiState.update { currentState ->
            currentState.copy(
                uCF_csvFileName = fileName,
                uCF_csvFileSize = fileSize,
                uCF_csvFileData = strList,
                uCF_errorState =
                    if (strList.size > Constants.MAX_CARDS) UCF_ErrorState.FILE_TOO_LONG
                    else UCF_ErrorState.NO_ERROR,
            )
        }
    }

    fun csvDataToCards(checkForErrors: Boolean = false): List<Card>? {
        if (checkForErrors && _uiState.value.uCF_csvFileData.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    uCF_errorState = UCF_ErrorState.FILE_EMPTY
                )
            }
            return null
        }

        val cards = mutableListOf<Card>()
        for (row in _uiState.value.uCF_csvFileData) {
            if (row.isNotEmpty()) {
                val qT = row.getOrNull(_uiState.value.uCF_questionIndex?.dec() ?: -1)
                val aT = row.getOrNull(_uiState.value.uCF_answerIndex?.dec() ?: -1)
                val hT = row.getOrNull(_uiState.value.uCF_hintIndex?.dec() ?: -1)
                val eT = row.getOrNull(_uiState.value.uCF_exampleIndex?.dec() ?: -1)

                if (checkForErrors && (qT == null || aT == null)) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            uCF_errorState = UCF_ErrorState.FILE_INCOMPLETE
                        )
                    }
                    return null
                }

                cards.add(
                    Card(
                        questionText = qT!!,
                        answerText = aT!!,
                        hintText = hT,
                        exampleText = eT,
                    )
                )
            }
        }

        if (cards.isEmpty() && checkForErrors) {
            toggleNoCardErrorDialog()
            return null
        }

        _uiState.update { currentState ->
            currentState.copy(
                uCF_errorState = UCF_ErrorState.NO_ERROR
            )
        }
        return cards
    }

    fun update() {
        _uiState.update { currentState ->
            currentState.copy(
                lastUpdated = System.currentTimeMillis(),
            )
        }
    }
}