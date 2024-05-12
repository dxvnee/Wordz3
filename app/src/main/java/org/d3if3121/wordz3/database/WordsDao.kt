package org.d3if3121.wordz3.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3121.wordz3.model.Words

@Dao
interface WordsDao {

    @Insert
    suspend fun insert(words: Words)

    @Update
    suspend fun update(words: Words)

    @Query("SELECT * FROM words ORDER BY word DESC")
    fun getWord(): Flow<List<Words>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: Long): Words?

    @Query("DELETE FROM words WHERE id = :id")
    suspend fun deleteWordById(id: Long)

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getWordCount(): Int




}