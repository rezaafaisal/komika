package com.example.storybookshelf

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class AddBookActivity : AppCompatActivity() {
    lateinit var db: BookDatabase
    lateinit var coverImage: ImageView
    lateinit var pickImageButton: Button
    lateinit var title: TextInputEditText
    lateinit var author: TextInputEditText
    lateinit var pageCount: TextInputEditText
    lateinit var description: TextInputEditText
    lateinit var saveButton: Button

    private fun initComponents(){
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()
        pickImageButton = findViewById(R.id.pick_image)
        coverImage = findViewById(R.id.cover_image)

        title = findViewById(R.id.input_title)
        author = findViewById(R.id.input_author)
        pageCount = findViewById(R.id.input_page)
        description = findViewById(R.id.input_description)
        saveButton = findViewById(R.id.button_save)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

//        instance components
        initComponents()

        val actionbar = supportActionBar
        actionbar?.setTitle(R.string.title_add_page)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        saveButton.setOnClickListener {
            GlobalScope.launch {
                val bookDao = db.bookDao()
                bookDao.insert(Book(
                    cover = coverImage.drawToBitmap(),
                    title = title.text.toString(),
                    author = author.text.toString(),
                    totalPages = pageCount.text.toString().toInt(),
                    description = description.text.toString()
                ))
            }

            Toast.makeText(applicationContext, R.string.success_message_insert, Toast.LENGTH_SHORT).show()

            navigateUpTo(Intent(applicationContext, MainActivity::class.java))
        }

        pickImageButton.setOnClickListener {
            pickContract.launch("image/*")
        }

    }

    private val pickContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            Log.d("dev", it.toString())
            coverImage.setImageURI(it)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}