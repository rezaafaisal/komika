package com.example.storybookshelf.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storybookshelf.database.dao.BookDao
import com.example.storybookshelf.database.entity.Book

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}