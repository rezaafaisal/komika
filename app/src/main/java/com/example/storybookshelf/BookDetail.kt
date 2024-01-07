package com.example.storybookshelf

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

//        set back button
        val actionbar = supportActionBar
        actionbar!!.title = "Comic Detail"
        actionbar.setDisplayHomeAsUpEnabled(true)
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

    private fun showDeleteDialog(id: Int){
        val builder = AlertDialog.Builder(applicationContext)
        builder.show()

        with(builder){
            setTitle("Yakin Hapus?")
            setMessage("Tindakan ini tidak dapat diurungkan!")
            setPositiveButton("Hapus"){dialog, which ->

//                delete book
                val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()

                GlobalScope.launch {
                    val bookDao = db.bookDao()
                    val book = bookDao.getBook(id)
                    bookDao.delete(book)
                }

                Toast.makeText(context, "Berhasil Hapus Komik!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
            setNegativeButton("Batal"){dialog, which ->
                Log.d("dev", "cancel")

                (context as MainActivity).finish()
//                val intent = Intent(context, MainActivity::class.java)
//                context.startActivity(intent)
            }
            show()
        }
    }
}