package com.example.notessqlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.database.CategoriesDatabase
import com.example.notessqlite.folders.FolderInFragmentAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FoldersAvailable : BottomSheetDialogFragment() {
    private lateinit var categoryAdapter: FolderInFragmentAdapter
    private lateinit var categoryDB: CategoriesDatabase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val availableFolders = view.findViewById<RecyclerView>(R.id.availableFolders)
        availableFolders.layoutManager = LinearLayoutManager(context)
        categoryDB =  context?.let { CategoriesDatabase(it) }!!
        categoryAdapter = FolderInFragmentAdapter(categoryDB.retrieveFolders(), requireContext())
        availableFolders.adapter = categoryAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_folders_available, container, false)
    }

    override fun onResume() {
        super.onResume()
        categoryAdapter.refreshData(categoryDB.retrieveFolders())
    }
}