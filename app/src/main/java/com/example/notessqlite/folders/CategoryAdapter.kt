package com.example.notessqlite.folders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.database.CategoriesDatabase
import com.example.notessqlite.folders.categoryviews.NotesInFolder
import com.example.notessqlite.folders.categoryviews.ViewNotesInFolder
import com.example.notessqlite.toasts.Utils

class CategoryAdapter(private var category: MutableList<Category>,context: Context):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var db:CategoriesDatabase = CategoriesDatabase(context)
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderName: TextView = itemView.findViewById(R.id.folderName)
        val dateModified: TextView = itemView.findViewById(R.id.dateModifiedzz)
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
            Utils.showToast(holder.itemView.context, "This action will have consequences.",R.drawable.butterfly_effect)
            Utils.showToast(holder.itemView.context,"$folderName deleted",R.drawable.ic_info)
        }
        holder.rigged.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewNotesInFolder::class.java).apply {
                putExtra("folder_Id",category.id)
            }
            holder.itemView.context.startActivity(intent)
            Utils.showToast(holder.itemView.context, "The butterfly effect.",R.drawable.butterfly_effect)
        }
        holder.card.setOnClickListener {
            if (category.folderName.contains("Entries")){
                holder.itemView.context.startActivity(Intent(holder.itemView.context,ViewNotesInFolder::class.java).apply {
                    putExtra("folder_Id",category.id)
                })
            }else{
                holder.itemView.context.startActivity(Intent(holder.itemView.context,NotesInFolder::class.java).apply {
                    putExtra("folder_Id",category.id)
                })
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newCategories: MutableList<Category>){
        category = newCategories
        notifyDataSetChanged()
    }
}