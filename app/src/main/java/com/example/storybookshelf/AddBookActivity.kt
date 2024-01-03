package com.example.storybookshelf

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {
    lateinit var db: BookDatabase
    lateinit var coverImage: ImageView
    lateinit var pickImageButton: Button
    lateinit var title: TextInputEditText
    lateinit var author: TextInputEditText
    lateinit var pageCount: TextInputEditText
    lateinit var saveButton: Button


    private fun initComponents(){
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()
        pickImageButton = findViewById(R.id.pick_image)
        coverImage = findViewById(R.id.cover_image)

        title = findViewById(R.id.input_title)
        author = findViewById(R.id.input_author)
        pageCount = findViewById(R.id.input_page)
        saveButton = findViewById(R.id.button_save)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

//        instance components
        initComponents()

        val actionbar = supportActionBar
        actionbar!!.title = "Tambah Koleksi Komik"
        actionbar.setDisplayHomeAsUpEnabled(true);

        saveButton.setOnClickListener {
            GlobalScope.launch {
                val bookDao = db.bookDao()
                bookDao.insert(Book(
                    title = title.text.toString(),
                    author = author.text.toString(),
                    totalPages = pageCount.text.toString().toInt()
                ))
            }

            Toast.makeText(applicationContext, "Berhasil menambahkan Komik!", Toast.LENGTH_SHORT).show()
            navigateUpTo(Intent(applicationContext, AddBookActivity::class.java))
        }

        pickImageButton.setOnClickListener {
            pickContract.launch("image/*")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val pickContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            coverImage.setImageURI(it)
        }
    }}