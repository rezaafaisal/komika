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
    lateinit var restart: Button
    @RequiresApi(34)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        permission launcher
        permissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))

//        init database
        db = Room.databaseBuilder(applicationContext, BookDatabase::class.java, "book-db").build()

        modelAdapter()

        floatingAddButton = findViewById(R.id.floating_add)

        floatingAddButton.setOnClickListener {
            startActivity(Intent(applicationContext, AddBookActivity::class.java))
        }

        restart = findViewById(R.id.restart)

        restart.setOnClickListener {
            recreate()
        }

    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){result ->

        var areAllGranted = true
        for (isGranted in result.values){
            Log.d("TAG", "tes")
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
            val bookAdapter = BookAdapter(this@MainActivity, newBooks, object : BookAdapter.OnAdapterListener{
                override fun onClick(book: Book) {

                    val bookDetailIntent = Intent(applicationContext, BookDetail::class.java)
                    bookDetailIntent.putExtra("book", book.toString())
                    startActivity(bookDetailIntent)
                }
            })

            findViewById<RecyclerView>(R.id.recycler_view).adapter = bookAdapter
        }
    }
}