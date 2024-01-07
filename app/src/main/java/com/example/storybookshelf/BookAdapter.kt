package com.example.storybookshelf

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.storybookshelf.database.BookDatabase
import com.example.storybookshelf.database.entity.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookAdapter(
    private val book: List<Book>,
    private val listener: OnAdapterListener
): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cover = view.findViewById<ImageView>(R.id.cover)
        val title = view.findViewById<TextView>(R.id.title)
        val author = view.findViewById<TextView>(R.id.author)
        val page = view.findViewById<TextView>(R.id.page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return book.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = book[position]

        holder.cover.setImageBitmap(book.cover)
        holder.title.text = book.title
        holder.author.text = "Author : ${book.author}"
        holder.page.text = "Total Pages : ${book.totalPages}"

        holder.itemView.setOnClickListener {
            listener.onClick(book)
        }


    }

    interface OnAdapterListener{
        fun onClick(book: Book)
    }
}


