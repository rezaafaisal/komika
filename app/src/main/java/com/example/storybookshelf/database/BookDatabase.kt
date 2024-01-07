package com.example.storybookshelf.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.storybookshelf.database.dao.BookDao
import com.example.storybookshelf.database.entity.Book

@Database(entities = [Book::class], version = 3)
@TypeConverters(BitmapConverter::class)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}