package com.example.notessqlite.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.NoteDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    private lateinit var db: NoteDatabase
    private lateinit var notesAdapter: NotesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetings: TextView = view.findViewById(R.id.greetings)
        val recyclerView: RecyclerView = view.findViewById(R.id.notesRView)
        val addBtn: FloatingActionButton = view.findViewById(R.id.addBtn)
        val searchView: SearchView = view.findViewById(R.id.searchView)
        val noDataView = view.findViewById<LinearLayout>(R.id.noDataView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { NoteDatabase(it) }!!
        notesAdapter = NotesAdapter(db.getAllNotes(), requireContext())
        recyclerView.adapter = notesAdapter

        addBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(intent)
        }
        searchView.queryHint = "Search Note..."

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (currentHour) {
            in 0..11 -> getString(R.string.greeting_morning)
            in 12..17 -> getString(R.string.greeting_afternoon)
            else -> getString(R.string.greeting_evening)
        }
        greetings.text = greeting

        searchView.setOnClickListener {
            Toast.makeText(context,"Tap on the search icon to the left to search!",Toast.LENGTH_SHORT).show()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }

            private fun filterData(searchTerm: String?) {
                val filteredList = if (searchTerm.isNullOrEmpty()){
                    db.getAllNotes()
                }else{
                    db.searchNote(searchTerm)
                }
                notesAdapter.updateData(filteredList)
                if (filteredList.isEmpty()){
                    noDataView.visibility = View.VISIBLE
                    addBtn.visibility = View.GONE
                }else{
                    noDataView.visibility = View.GONE
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}