package com.example.notessqlite.archives

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.Utils
import com.example.notessqlite.databases.ArchivesDatabase
import com.example.notessqlite.databases.NoteDatabase
import com.example.notessqlite.notes.Note

class ArchivesAdapter(private var notes: MutableList<Note>, context: Context) : RecyclerView.Adapter<ArchivesAdapter.ArchiveViewHolder>() {
    class ArchiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val idTVDate: TextView = itemView.findViewById(R.id.idTVDate)
//        val card: LinearLayout = itemView.findViewById(R.id.what)
        val unarchiveButton: ImageView = itemView.findViewById(R.id.archiveButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    private val db:ArchivesDatabase = ArchivesDatabase(context)
    private val notesDB:NoteDatabase = NoteDatabase(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ArchiveViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.idTVDate.text = note.time
        holder.deleteButton.visibility = View.GONE

        holder.unarchiveButton.setOnClickListener {
            val title = note.title
            note.id.let { it1->db.deleteNote(it1) }
            notesDB.insertNote(note)
            refreshData(db.getArchivedNotes())
            Utils.showToast(holder.itemView.context,"Protege took note of your realization of this note.",R.drawable.ic_info)
            Utils.showToast(holder.itemView.context,"$title has been unarchived.",R.drawable.ic_info)
        }
    }

    override fun getItemCount(): Int = notes.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: MutableList<Note>) {
        notes =  newNotes
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList:List<Note>){
        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }
}