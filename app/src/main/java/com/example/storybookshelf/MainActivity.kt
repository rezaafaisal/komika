package com.example.storybookshelf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var db: BookDatabase
    lateinit var floatingAddButton: FloatingActionButton
    @RequiresApi(34)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        permission launcher
        permissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))

//        init components
        initComponents()
        modelAdapter()

        floatingAddButton.setOnClickListener {
            startActivity(Intent(applicationContext, AddBookActivity::class.java))
        }

    }

    private fun initComponents(){
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db")
            .fallbackToDestructiveMigration()
            .build()
        floatingAddButton = findViewById(R.id.floating_add)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){result ->

        var areAllGranted = true
        for (isGranted in result.values){
            areAllGranted = areAllGranted && isGranted
        }

        if (areAllGranted){
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }


    private fun modelAdapter(){
        GlobalScope.launch {
            val newBooks = db.bookDao().getAll()
            val bookAdapter = BookAdapter(newBooks, object : BookAdapter.OnAdapterListener{
                override fun onClick(book: Book) {
                    val bookDetailIntent = Intent(applicationContext, BookDetail::class.java)
                    bookDetailIntent.putExtra("id", book.id)
                    bookDetailIntent.putExtra("cover", book.cover)
                    bookDetailIntent.putExtra("title", book.title)
                    bookDetailIntent.putExtra("author", book.author)
                    bookDetailIntent.putExtra("pages", book.totalPages)
                    bookDetailIntent.putExtra("description", book.description)

                    startActivity(bookDetailIntent)
                }
            })

            findViewById<RecyclerView>(R.id.recycler_view).adapter = bookAdapter
        }
    }


}