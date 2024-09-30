package com.example.notessqlite.notes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
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
        val searchView: EditText = view.findViewById(R.id.searchView)
        val no = mutableListOf<Note>()
        val noteArray = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, android.R.id.title, no)

        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { NoteDatabase(it) }!!
        notesAdapter = NotesAdapter(db.getAllNotes(), requireContext())
        recyclerView.adapter = notesAdapter

        addBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(intent)
        }
        searchView.addTextChangedListener(object:TextWatcher
        {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                noteArray.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun performFiltering(query: String) {
//        val filteredList = fi
    }
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}