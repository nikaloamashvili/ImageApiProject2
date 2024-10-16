package com.example.fullproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fullproject.model.Hit


@Database(entities = [Hit::class], version = 1)

abstract class AppDatabase:RoomDatabase() {
        abstract fun HitDao(): HitDao
}