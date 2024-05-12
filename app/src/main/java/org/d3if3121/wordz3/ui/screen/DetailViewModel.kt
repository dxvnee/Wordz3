package org.d3if3121.wordz3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3121.wordz3.database.WordsDao
import org.d3if3121.wordz3.model.Words
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: WordsDao) : ViewModel() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(wordInput: String, meaningInput: String, noteInput: String){
        val words = Words(
            dateAdded = "",
            word = wordInput,
            meaning = meaningInput,
            note = noteInput
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.insert(words)
        }
    }
    fun insertWithDate(wordInput: String, meaningInput: String, noteInput: String){
        val words = Words(
            dateAdded = formatter.format(Date()),
            word = wordInput,
            meaning = meaningInput,
            note = noteInput
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.insert(words)
        }
    }
    suspend fun getWords(id: Long): Words? {
         return dao.getWordById(id)
    }

    fun update(id: Long, wordInput: String, meaningInput: String, noteInput: String){
        val catatan = Words(
            id = id,
            dateAdded = formatter.format(Date()),
            word = wordInput,
            meaning = meaningInput,
            note = noteInput

        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(catatan)
        }

    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteWordById(id)
        }
    }

    suspend fun getWordCount(): Int {

        return dao.getWordCount()

    }

}