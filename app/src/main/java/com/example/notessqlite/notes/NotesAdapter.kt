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
import com.example.notessqlite.toasts.CodeBase
import com.example.notessqlite.R
import com.example.notessqlite.database.ArchivesDatabase
import com.example.notessqlite.database.EntryDatabase
import com.example.notessqlite.database.InsertNoteIntoFolderDatabase
import com.example.notessqlite.database.NoteDatabase
import com.example.notessqlite.database.TrashDatabase

class NotesAdapter(private var notes: MutableList<Note>,context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabase = NoteDatabase(context)
    private val archiveDB: ArchivesDatabase = ArchivesDatabase(context)
    private val insertDB:InsertNoteIntoFolderDatabase = InsertNoteIntoFolderDatabase(context)
    private val trashDB:TrashDatabase = TrashDatabase(context)

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

        holder.card.setOnClickListener {
            try{
                val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                    putExtra("note_id", note.id)
                }
                holder.itemView.context.startActivity(intent)
            }catch (_:Exception){
                CodeBase.showToast(holder.itemView.context,"Wrong activity callback",R.drawable.ic_info)
            }finally {
                CodeBase.showToast(holder.itemView.context,"Wrong activity callback",R.drawable.ic_info)
            }
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
            trashDB.addNoteIntoTrash(note)
            refreshData(db.getAllNotes())
            CodeBase.showToast(holder.itemView.context, "This action will have consequences.", R.drawable.butterfly_effect)
            CodeBase.showToast(holder.itemView.context, "$title deleted!", R.drawable.ic_info)
        }
        val randomColor = getRandomColor()
        val borderDrawable = GradientDrawable()
        borderDrawable.setStroke(1,randomColor)
        borderDrawable.cornerRadius = 10f
//        holder.card.background = borderDrawable
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