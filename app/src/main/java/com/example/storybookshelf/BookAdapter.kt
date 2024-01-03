package com.example.storybookshelf

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.storybookshelf.database.entity.Book

class BookAdapter(
    private val context: Context,
    private val book: List<Book>,
    private val listener: OnAdapterListener
): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.title)
        val author = view.findViewById<TextView>(R.id.author)
        val page = view.findViewById<TextView>(R.id.page)
        val optionButton = view.findViewById<Button>(R.id.button_option)
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

        holder.optionButton.setOnClickListener {
            showPopupMenu(it, book.id)
        }

        holder.itemView.setOnClickListener {
            listener.onClick(book)
        }

    }

    interface OnAdapterListener{
        fun onClick(book: Book)
    }

    private fun showPopupMenu(view: View, id: Int?) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.option, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.update -> {
                    // Handle menu item 1 click
                    Log.d("dev", id.toString())
                    context.startActivity(Intent(context, EditBookActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//                    Toast.makeText(context, "Update Berhasil", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.delete -> {
                    Toast.makeText(context, "Delete Berhasil", Toast.LENGTH_SHORT).show()
                    true
                }
                // Add more cases for other menu items if needed
                else -> false
            }
        }
        popupMenu.show()
    }

}

