package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CampusEvent
import com.example.data.model.CampusNews
import com.example.data.model.CampusVisit
import com.example.data.model.EventRegistration
import com.example.data.model.Lead
import com.example.data.model.RagDocument

@Database(
    entities = [
        Lead::class,
        CampusVisit::class,
        CampusEvent::class,
        RagDocument::class,
        CampusNews::class,
        EventRegistration::class
    ],
    version = 4,
    exportSchema = false
)
abstract class SantotoDatabase : RoomDatabase() {

    abstract fun santotoDao(): SantotoDao

    companion object {
        @Volatile
        private var INSTANCE: SantotoDatabase? = null

        fun getDatabase(context: Context): SantotoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SantotoDatabase::class.java,
                    "santoto_tunja_admissions.db"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
