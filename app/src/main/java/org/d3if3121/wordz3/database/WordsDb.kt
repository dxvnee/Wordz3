package org.d3if3121.wordz3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3121.wordz3.model.Words

@Database(entities = [Words::class], version = 1, exportSchema = false)
abstract class WordsDb : RoomDatabase(){

    abstract val dao: WordsDao

    companion object {

        @Volatile
        private var INSTANCE: WordsDb? = null

        fun getInstance(context: Context): WordsDb {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordsDb::class.java,
                        "words.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}