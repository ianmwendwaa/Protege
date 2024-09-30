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

class ToDoAdapter(private var todo: List<ToDo>, context: Context) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private val db: ToDoDatabase = ToDoDatabase(context)

    class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.todotitleTextView)
        val contentText: TextView = view.findViewById(R.id.todocontentTextView)
        //val updateButton = view.findViewById<Button>(R.id.updateButton)
        val elapsedTime: TextView = view.findViewById(R.id.elapsedtime)
        val deleteButton: ImageView = view.findViewById(R.id.deletetodoButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
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
            Toast.makeText(holder.itemView.context, "$title deleted", Toast.LENGTH_SHORT).show()
        }
//        holder.updateButton.setOnClickListener {
//            val intent = Intent(holder.itemView.context, UpdateToDoActivity::class.java).apply {)
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newToDos: List<ToDo>) {
        todo = newToDos
        notifyDataSetChanged()
    }
}