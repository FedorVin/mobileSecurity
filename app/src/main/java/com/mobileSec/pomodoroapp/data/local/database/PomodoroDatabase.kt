package com.mobileSec.pomodoroapp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.mobileSec.pomodoroapp.data.local.dao.TimerSessionDao
import com.mobileSec.pomodoroapp.data.local.dao.TodoDao
import com.mobileSec.pomodoroapp.data.local.dao.UserDao
import com.mobileSec.pomodoroapp.data.local.entity.TimerSessionEntity
import com.mobileSec.pomodoroapp.data.local.entity.UserEntity
import com.mobileSec.pomodoroapp.data.local.dao.*
import com.mobileSec.pomodoroapp.data.local.entity.*

@Database(
    entities = [TodoEntity::class, TimerSessionEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun timerSessionDao(): TimerSessionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: PomodoroDatabase? = null

        fun getDatabase(context: Context): PomodoroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PomodoroDatabase::class.java,
                    "pomodoro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}