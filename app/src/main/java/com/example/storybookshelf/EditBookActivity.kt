package com.example.storybookshelf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditBookActivity : AppCompatActivity() {
    private lateinit var db: BookDatabase
    private lateinit var coverImage: ImageView
    private lateinit var pickImageButton: Button
    private lateinit var title: TextInputEditText
    private lateinit var author: TextInputEditText
    private lateinit var pageCount: TextInputEditText
    private lateinit var saveButton: Button

    private lateinit var book: Book
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        initComponents()

        val id: Int = intent.getIntExtra("id", 1)

        GlobalScope.launch {
            val bookDao = db.bookDao()
            book = bookDao.getBook(id)

            GlobalScope.launch(Dispatchers.Main) {
                title.text = book.title.toEditable()
                author.text = book.author.toEditable()
                pageCount.text = book.totalPages.toString().toEditable()
            }
        }

        saveButton.setOnClickListener {
            GlobalScope.launch {
                db.bookDao().update(
                    id = id,
                    title = title.text.toString(),
                    author = author.text.toString(),
                    pages = pageCount.text.toString().toInt()
                )
            }

            Toast.makeText(applicationContext, "Berhasil perbarui Komik!", Toast.LENGTH_SHORT).show()
            navigateUpTo(Intent(applicationContext, MainActivity::class.java))
        }


    }

    private fun initComponents(){
        //        init database
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()
        coverImage = findViewById(R.id.cover_image)
        pickImageButton = findViewById(R.id.pick_image)
        title = findViewById(R.id.edit_title)
        author = findViewById(R.id.edit_author)
        pageCount = findViewById(R.id.edit_page)
        saveButton = findViewById(R.id.button_update)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}