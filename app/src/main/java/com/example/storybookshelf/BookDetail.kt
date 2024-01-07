package com.example.storybookshelf

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookDetail : AppCompatActivity() {
    private lateinit var db: BookDatabase
    private lateinit var cover: ImageView
    private lateinit var title: TextView
    private lateinit var author: TextView
    private lateinit var description: TextView
    private lateinit var pages: TextView
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

//        init components
        initComponents()

//        get id from intent
        val id: Int = intent.getIntExtra("id", 0)

//        set data
        GlobalScope.launch {
            val bookDao = db.bookDao()
            book = bookDao.getBook(id)

            GlobalScope.launch(Dispatchers.Main) {
                title.text = book.title
                author.text = book.author
                pages.text = book.totalPages.toString()
                cover.setImageBitmap(book.cover)
            }
        }


//        set back button
        val actionbar = supportActionBar
        actionbar!!.title = "Comic Detail"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponents(){
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()
        cover = findViewById(R.id.cover_image)
        title = findViewById(R.id.title)
        author = findViewById(R.id.author)
        description = findViewById(R.id.description)
        pages = findViewById(R.id.pages)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.delete -> {
                showDeleteDialog()
                true
            }

            R.id.update -> {
                val bookDetailIntent = Intent(applicationContext, EditBookActivity::class.java)
                bookDetailIntent.putExtra("id", book.id)
                startActivity(bookDetailIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showDeleteDialog(){
        val builder = AlertDialog.Builder(this)

        with(builder){
            setTitle("Sure want to delete?")
            setMessage("This action cannot be undone!")
            setPositiveButton("Yes"){dialog, which ->
//                delete book
                val db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()

                GlobalScope.launch {
                    val bookDao = db.bookDao()
                    val book = bookDao.getBook(book.id!!)
                    bookDao.delete(book)
                }

                Toast.makeText(applicationContext, R.string.success_message_delete, Toast.LENGTH_SHORT).show()
                navigateUpTo(Intent(applicationContext, MainActivity::class.java))
            }
            setNegativeButton("Cancel"){dialog, which ->
                Log.d("dev", "cancel")
            }
            show()
        }
    }
}