package com.example.storybookshelf.database.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
//    @ColumnInfo(name = "cover") val cover: Bitmap,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name="total_pages") val totalPages: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
