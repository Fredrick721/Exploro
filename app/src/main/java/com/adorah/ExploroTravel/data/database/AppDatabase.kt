package com.example.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adorah.ExploroTravel.models.ExploroItem


// Add annotation database to mark this class as the database migration
// layer
@Database(
    entities = [ExploroItem::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    // define an abstract function for the database interface


}










