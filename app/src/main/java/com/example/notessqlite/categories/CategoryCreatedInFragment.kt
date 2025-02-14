package com.example.notessqlite.categories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.Utils
import com.example.notessqlite.databases.CategoriesDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import com.example.notessqlite.notes.Note
import com.example.notessqlite.notes.NotesAdapter as NotesAdapter1

class CategoryCreatedInFragment(private var category: MutableList<Category>, context: Context):RecyclerView.Adapter<CategoryCreatedInFragment.CategoryViewHolder>() {
    private var db:CategoriesDatabase = CategoriesDatabase(context)
    private var insertNoteIntoFolderDatabase:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)
    private var notes:Note = Note(id = 0, "", "", "","")
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderName: TextView = itemView.findViewById(R.id.folderName)
        val dateModified: TextView = itemView.findViewById(R.id.dateModified)
        val card:LinearLayout = itemView.findViewById(R.id.card)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item_viewed,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = category.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = category[position]
        holder.folderName.text = category.folderName
        holder.dateModified.text = category.folderDescription

        holder.card.setOnClickListener {
            try{
                insertNoteIntoFolderDatabase.insertIntoFolder(notes)
            }catch (e:Exception){
                Utils.showToast(holder.itemView.context, "$e",R.drawable.ic_info)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newCategories: MutableList<Category>){
        category = newCategories
        notifyDataSetChanged()
    }
}