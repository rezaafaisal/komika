package com.example.storybookshelf.database.dao

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.storybookshelf.database.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll():List<Book>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBook(id: Int) : Book

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg books: Book)

    @Query("UPDATE book SET cover=:cover,  title=:title, author=:author, total_pages=:pages, description=:description WHERE id=:id")
    fun update(id: Int,
               title: String,
               author: String,
               pages: Int,
               description: String,
               cover: Bitmap)

    @Delete
    fun delete(book: Book)
}