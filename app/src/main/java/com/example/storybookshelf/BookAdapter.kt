package com.example.storybookshelf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storybookshelf.database.entity.Book

class BookAdapter(
    private val book: List<Book>,
    private val listener: OnAdapterListener
): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
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

        holder.title.text = book.title
        holder.author.text = book.author
        holder.page.text = book.totalPages.toString()

        holder.itemView.setOnClickListener {
            listener.onClick(book)
        }

    }

    interface OnAdapterListener{
        fun onClick(book: Book)
    }
}

