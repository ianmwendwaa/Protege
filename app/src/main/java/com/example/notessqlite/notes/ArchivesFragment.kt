package com.example.notessqlite.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.ArchivesDatabase
import com.example.notessqlite.databases.NoteDatabase

class ArchivesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archives, container, false)
    }

    private lateinit var db:ArchivesDatabase
    private lateinit var archivesAdapter: ArchivesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView= view.findViewById(R.id.archivedRVView)
        val noArchives:LinearLayout = view.findViewById(R.id.noArchived)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { ArchivesDatabase(it) }!!
        archivesAdapter = ArchivesAdapter(db.getArchivedNotes(),requireContext())
        recyclerView.adapter = archivesAdapter
        val archivedQueryListResult = db.getArchivedNotes()
        if(archivedQueryListResult.isEmpty()){
            archivesAdapter.refreshData(db.getArchivedNotes())
            noArchives.visibility = View.VISIBLE
        }else{
            noArchives.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        archivesAdapter.refreshData(db.getArchivedNotes())
    }
}