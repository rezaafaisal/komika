package com.example.storybookshelf

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers


class EditBookActivity : AppCompatActivity() {
    private lateinit var db: BookDatabase
    private lateinit var cover: ImageView
    private lateinit var pickImageButton: Button
    private lateinit var title: TextInputEditText
    private lateinit var author: TextInputEditText
    private lateinit var pages: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var book: Book
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

//        init component
        initComponents()

//        set toolbar action
        val actionbar = supportActionBar
        actionbar?.setTitle(R.string.title_edit_page)
        actionbar?.setDisplayHomeAsUpEnabled(true)

//        get id from intent
        val id: Int = intent.getIntExtra("id", 1)

        GlobalScope.launch {
            val bookDao = db.bookDao()
            book = bookDao.getBook(id)

            GlobalScope.launch(Dispatchers.Main) {
                title.text = book.title.toEditable()
                author.text = book.author.toEditable()
                pages.text = book.totalPages.toString().toEditable()
                description.text = book.description.toEditable()
                cover.setImageBitmap(book.cover)
            }
        }

//        save change action
        saveButton.setOnClickListener {
            GlobalScope.launch {
                db.bookDao().update(
                    id = book.id!!,
                    title = title.text.toString(),
                    author = author.text.toString(),
                    pages = pages.text.toString().toInt(),
                    description = description.text.toString(),
                    cover = cover.drawToBitmap()
                )
            }

            Toast.makeText(applicationContext, R.string.success_message_update, Toast.LENGTH_SHORT).show()
            navigateUpTo(Intent(applicationContext, MainActivity::class.java))
        }

//        pick image from gallery
        pickImageButton.setOnClickListener {
            pickContract.launch("image/*")
        }


    }

    private fun initComponents(){
        //        init database
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db")
            .fallbackToDestructiveMigration()
            .build()
        cover = findViewById(R.id.cover_image)
        pickImageButton = findViewById(R.id.pick_image)
        title = findViewById(R.id.edit_title)
        author = findViewById(R.id.edit_author)
        pages = findViewById(R.id.edit_page)
        description = findViewById(R.id.edit_description)
        saveButton = findViewById(R.id.button_update)
    }

    private val pickContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            Log.d("dev", it.toString())
            cover.setImageURI(it)
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

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}