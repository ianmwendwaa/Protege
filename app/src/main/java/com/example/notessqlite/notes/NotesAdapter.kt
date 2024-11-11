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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.ArchivesDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import com.example.notessqlite.databases.NoteDatabase

class NotesAdapter(private var notes: MutableList<Note>,private val  fragmentManager: FragmentManager,context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabase = NoteDatabase(context)
    private val archiveDB: ArchivesDatabase = ArchivesDatabase(context)
//    private val categoryDB:CategoriesDatabase = CategoriesDatabase(context)
    private val insertDB:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val idTVDate: TextView = itemView.findViewById(R.id.idTVDate)
        val card: LinearLayout = itemView.findViewById(R.id.what)
        val archiveButton: ImageView = itemView.findViewById(R.id.archiveButton)
        val moreButton:ImageView = itemView.findViewById(R.id.displayMore)
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
//        holder.card.setOnLongClickListener {
//            val bottomSheetFragment = DialogActionPrompt()
//            true
//        }
        holder.moreButton.setOnClickListener {
            val bottomSheetFragment = DialogActionPrompt()
            bottomSheetFragment.show(fragmentManager,bottomSheetFragment.tag)
        }

        holder.archiveButton.setOnClickListener{
            val title = note.title
            note.id.let { it1 -> db.deleteNote(it1) }
            archiveDB.insertArchivedNote(note)
            insertDB.insertIntoFolder(note)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context,"$title archived",Toast.LENGTH_SHORT).show()
        }

        holder.deleteButton.setOnClickListener {
            val title = note.title
            note.id.let { it1 -> db.deleteNote(it1) }
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "$title deleted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: MutableList<Note>) {
        notes =  newNotes
        notifyDataSetChanged()
    }
//    To be used in updating searchView
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList:List<Note>){
        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }
}