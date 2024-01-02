package com.example.storybookshelf.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storybookshelf.R
import com.example.storybookshelf.database.entity.Book

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var listBook = emptyList<Book>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    override fun onBindViewHolder(holder: ListAdapter.MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}