package com.example.notessqlite.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    private lateinit var db: NoteDatabase
    private lateinit var notesAdapter: NotesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.notesRView)
        val addBtn: FloatingActionButton = view.findViewById(R.id.addBtn)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { NoteDatabase(it) }!!
        notesAdapter = NotesAdapter(db.getAllNotes(), requireContext())
        recyclerView.adapter = notesAdapter

        addBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}