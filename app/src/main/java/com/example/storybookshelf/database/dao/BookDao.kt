package com.example.storybookshelf.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.storybookshelf.database.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll():List<Book>

    @Insert
    fun insert(vararg books: Book)

    @Delete
    fun delete(book: Book)
}