package com.example.notessqlite.categories

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.categories.categoryviews.ViewNotesInFolder
import com.example.notessqlite.databases.CategoriesDatabase

class CategoryAdapter(private var category: MutableList<Category>,context: Context):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var db:CategoriesDatabase = CategoriesDatabase(context)
//    private var insertNoteIntoFolderDatabase:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderName: TextView = itemView.findViewById(R.id.folderName)
        val dateModified: TextView = itemView.findViewById(R.id.date_Modifiedzz)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteFolder)
        val rigged:ImageView = itemView.findViewById(R.id.rigggeeeeddddd)
        val card:LinearLayout = itemView.findViewById(R.id.card)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = category.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = category[position]
        holder.folderName.text = category.folderName
        holder.dateModified.text = category.dateModified

        holder.deleteButton.setOnClickListener {
            val folderName = category.folderName
            category.id.let { it1->db.deleteFolder(it1) }
            refreshData(db.retrieveFolders())
            Toast.makeText(holder.itemView.context,"$folderName deleted",Toast.LENGTH_SHORT).show()
        }
        holder.rigged.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewNotesInFolder::class.java).apply {
                putExtra("folder_Id",category.id)
            }
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context,"Not Yet There boy😝",Toast.LENGTH_SHORT).show()
        }
        holder.card.setOnClickListener {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newCategories: MutableList<Category>){
        category = newCategories
        notifyDataSetChanged()
    }
}