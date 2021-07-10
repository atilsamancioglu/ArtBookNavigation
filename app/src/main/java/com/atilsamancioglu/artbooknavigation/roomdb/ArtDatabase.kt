package com.atilsamancioglu.artbooknavigation.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atilsamancioglu.artbooknavigation.model.Art
import com.atilsamancioglu.artbooknavigation.roomdb.ArtDao


@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}
