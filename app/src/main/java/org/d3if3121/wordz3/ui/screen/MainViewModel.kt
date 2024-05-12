package org.d3if3121.wordz3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3121.wordz3.database.WordsDao
import org.d3if3121.wordz3.model.Words

class MainViewModel(dao: WordsDao) : ViewModel() {
    val data: StateFlow<List<Words>> = dao.getWord().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}