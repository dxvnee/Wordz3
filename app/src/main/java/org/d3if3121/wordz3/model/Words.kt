package org.d3if3121.wordz3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Words(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val word: String,
    val meaning: String,
    val note: String,
    val dateAdded: String

)
