package com.example.notessqlite.archives.ui.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.database.NoteDatabase
import com.example.notessqlite.database.TrashDatabase
import com.example.notessqlite.databinding.FragmentTrashBinding
import com.example.notessqlite.notes.NoteRepository
import com.example.notessqlite.notes.NoteViewModel
import com.example.notessqlite.notes.NotesAdapter

class TrashFragment : Fragment() {

    private var _binding: FragmentTrashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrashBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: NotesAdapter
    private lateinit var db:TrashDatabase
    private lateinit var db1:NoteDatabase
    private val noteId = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val header = "Trash will be cleared after 7 days!"
        binding.header.text = header
        db = context?.let { TrashDatabase(it) }!!
        db1 = context?.let { NoteDatabase(it) }!!
        adapter = NotesAdapter(db.getAllDisposedTrash(),requireContext(), viewModel = NoteViewModel(noteRepo = NoteRepository(db1)))
        binding.trashRV.layoutManager = LinearLayoutManager(context)
        binding.trashRV.adapter = adapter
        val expirationTarget = java.util.concurrent.TimeUnit.DAYS.toMillis(7)
        val time = System.currentTimeMillis()
        val threshold = expirationTarget - time
        if (threshold.toInt() == 0){
            db.destroyNotes(noteId)
        }
    }
    override fun onResume() {
        super.onResume()
        adapter.refreshData(db.getAllDisposedTrash())
    }
}