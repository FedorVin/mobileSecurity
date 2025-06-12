package com.mobilesec.pomodorotimer.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [TodoEntity::class, TimerSessionEntity::class, UserCredentialsEntity::class], // Keep original entities
    version = 1, // Keep version 1 to avoid migration issues
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun timerSessionDao(): TimerSessionDao
    abstract fun userSettingsDao(): UserCredentialsDao // Renamed but using original DAO type

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

