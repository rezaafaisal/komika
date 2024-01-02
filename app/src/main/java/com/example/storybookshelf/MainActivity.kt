package com.example.storybookshelf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }


    private fun modelAdapter(){
        val books = listOf<Book>(
            Book("Naruto", "Masashi Kisimoto", 23),
            Book("Attack on Titan", "Hajime Isayama", 255),
        )

        GlobalScope.launch {
            val newBooks = db.bookDao().getAll()

            Log.e("MainActivity", newBooks[0].toString())
            val bookAdapter = BookAdapter(newBooks, object : BookAdapter.OnAdapterListener{
                override fun onClick(book: Book) {
                    Log.d("MainActivity", book.toString())
                }

            })

            findViewById<RecyclerView>(R.id.recycler_view).adapter = bookAdapter
        }


    }


}