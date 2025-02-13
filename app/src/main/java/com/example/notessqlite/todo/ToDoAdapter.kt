package com.example.notessqlite.todo

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
import com.example.notessqlite.databases.ToDoDatabase

class ToDoAdapter(private var todo: MutableList<ToDo>, context: Context) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private val db: ToDoDatabase = ToDoDatabase(context)

    class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleTextView)
        val contentText: TextView = view.findViewById(R.id.contentTextView)
        //val updateButton = view.findViewById<Button>(R.id.updateButton)
        val elapsedTime: TextView = view.findViewById(R.id.idTVDate)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ToDoViewHolder(view)
    }

    override fun getItemCount(): Int = todo.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todo[position]
        holder.titleText.text = todo.taskName
        holder.contentText.text = todo.taskDescription
        holder.elapsedTime.text = todo.time

        holder.deleteButton.setOnClickListener {
            val title = todo.taskName
            todo.id.let { it1 ->
                if (it1 != null) {
                    db.deleteToDo(it1)
                }
            }
            refreshData(db.getAllToDos())
            Utils.showToast(holder.itemView.context, "$title deleted", R.drawable.ic_info)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newToDos: MutableList<ToDo>) {
        todo = newToDos
        notifyDataSetChanged()
    }
}