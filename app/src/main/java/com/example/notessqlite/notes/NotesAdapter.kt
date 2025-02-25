package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.CodeBase
import com.example.notessqlite.R
import com.example.notessqlite.databases.ArchivesDatabase
import com.example.notessqlite.databases.EntryDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import com.example.notessqlite.databases.NoteDatabase

class NotesAdapter(private var notes: MutableList<Note>,private val  fragmentManager: FragmentManager,context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabase = NoteDatabase(context)
    private val archiveDB: ArchivesDatabase = ArchivesDatabase(context)
    private val insertDB:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)

    private val entryDB:EntryDatabase = EntryDatabase(context)

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

        if(note.title=="Entry"){
            entryDB.insertNote(note)
        }

        holder.card.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
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
            CodeBase.showToast(holder.itemView.context, "This action will have consequences.", R.drawable.butterfly_effect)
            CodeBase.showToast(holder.itemView.context, "$title has been archived.", R.drawable.ic_info)
        }

        holder.deleteButton.setOnClickListener {
            val title = note.title
            note.id.let { it1 -> db.deleteNote(it1) }
            refreshData(db.getAllNotes())
            CodeBase.showToast(holder.itemView.context, "This action will have consequences.", R.drawable.butterfly_effect)
            CodeBase.showToast(holder.itemView.context, "$title deleted", R.drawable.ic_info)
        }
        val randomColor = getRandomColor()
        val borderDrawable = GradientDrawable()
        borderDrawable.setStroke(3,randomColor)
        borderDrawable.cornerRadius = 10f
//        holder.cardView.background = borderDrawable
    }

    private fun getRandomColor(): Int {
        val random = java.util.Random()
        return Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256))
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