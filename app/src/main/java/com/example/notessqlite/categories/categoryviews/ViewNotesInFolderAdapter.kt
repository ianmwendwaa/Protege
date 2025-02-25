package com.example.notessqlite.categories.categoryviews

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
import com.example.notessqlite.Utils
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import com.example.notessqlite.databases.NoteDatabase
import com.example.notessqlite.notes.Note
import com.example.notessqlite.notes.UpdateNoteActivity

class ViewNotesInFolderAdapter(private var notes: MutableList<Note>, context: Context) : RecyclerView.Adapter<ViewNotesInFolderAdapter.CategoryPreviewViewHolder>() {
    class CategoryPreviewViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val idTVDate: TextView = itemView.findViewById(R.id.idTVDate)
        val card: LinearLayout = itemView.findViewById(R.id.what)
        val unarchiveButton: ImageView = itemView.findViewById(R.id.archiveButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    private val db:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)
    private val notesDB:NoteDatabase = NoteDatabase(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryPreviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return CategoryPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryPreviewViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.idTVDate.text = note.time

//        holder.card.setOnClickListener{
//            try {
//                holder.itemView.context.startActivity(Intent(holder.itemView.context,UpdateNoteActivity::class.java).apply {
//                    putExtra("note_id",note.id)
//                })
//            }catch (e:Exception){
//                Utils.showToast(holder.itemView.context,"Error $e occurred",R.drawable.ic_info)
//            }
//        }

        holder.unarchiveButton.setOnClickListener {
            val title = note.title
            note.id.let { it1->db.deleteNote(it1) }
            notesDB.insertNote(note)
            refreshData(db.getFoldersNotes())
            Toast.makeText(holder.itemView.context,"$title unarchived",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = notes.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: MutableList<Note>) {
        notes =  newNotes
        notifyDataSetChanged()
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newList:MutableList<Note>){
//        notes.clear()
//        notes.addAll(newList)
//        notifyDataSetChanged()
//    }
}