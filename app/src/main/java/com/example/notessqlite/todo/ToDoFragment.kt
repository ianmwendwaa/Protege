package com.example.notessqlite.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.ToDoDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }
    private lateinit var db: ToDoDatabase
    private lateinit var toDoAdapter: ToDoAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addTask: FloatingActionButton = view.findViewById(R.id.todoAddBtn)
        val recyclerView: RecyclerView = view.findViewById(R.id.todoRView)
        val noToDo: LinearLayout = view.findViewById(R.id.noToDo)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { ToDoDatabase(it) }!!
        toDoAdapter = ToDoAdapter(db.getAllToDos(), requireContext())
        recyclerView.adapter = toDoAdapter
        val nullToDoQueryListResult = db.getAllToDos()
        if(nullToDoQueryListResult.isEmpty()){
            noToDo.visibility = View.VISIBLE
        }else{
            noToDo.visibility = View.GONE
        }
        addTask.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager,bottomSheetFragment.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        toDoAdapter.refreshData(db.getAllToDos())
    }
}