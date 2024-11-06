package com.example.notessqlite.notes

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
import com.example.notessqlite.databases.NoteDatabase

class NotesAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabase = NoteDatabase(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val idTVDate: TextView = itemView.findViewById(R.id.idTVDate)
        val card: LinearLayout = itemView.findViewById(R.id.what)
        //val charCounter:TextView = itemView.findViewById(R.id.charCount)
        val archiveButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.idTVDate.text = note.time

        holder.card.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.archiveButton.setOnClickListener{
            var isArchived = false
            val item = notes[position]
            notifyItemChanged(position)
        }

        holder.deleteButton.setOnClickListener {
            val title = note.title
            note.id.let { it1 -> db.deleteNote(it1) }
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "$title deleted", Toast.LENGTH_SHORT).show()
            //Snackbar.make(holder.itemView.context, "$title deleted", Snackbar.LENGTH_LONG).show()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>) {
        notes =  newNotes
        notifyDataSetChanged()
    }
}