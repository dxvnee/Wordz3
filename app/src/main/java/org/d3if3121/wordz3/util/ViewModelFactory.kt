package org.d3if3121.wordz3.util


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3121.wordz3.database.WordsDao
import org.d3if3121.wordz3.ui.screen.DetailViewModel
import org.d3if3121.wordz3.ui.screen.MainViewModel

class ViewModelFactory (
    private val dao: WordsDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}