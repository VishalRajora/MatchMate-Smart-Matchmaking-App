package com.example.matchmate.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.matchmate.modelclass.Converters
import com.example.matchmate.modelclass.MateMatchResultRoom


@Database(entities = [MateMatchResultRoom::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
