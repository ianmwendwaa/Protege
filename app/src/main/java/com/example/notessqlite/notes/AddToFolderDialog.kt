package com.example.notessqlite.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.folders.CategoryCreatedInFragment
import com.example.notessqlite.database.CategoriesDatabase
import com.example.notessqlite.database.InsertNoteIntoFolderDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class AddToFolderDialog:BottomSheetDialogFragment() {
    private lateinit var insertDB: InsertNoteIntoFolderDatabase
    private lateinit var fetchFolderDB:CategoriesDatabase
    private lateinit var adapterClass: CategoryCreatedInFragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView:RecyclerView = view.findViewById(R.id.foldersToChooseFrom)
        fetchFolderDB = context?.let { CategoriesDatabase(it) }!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapterClass = CategoryCreatedInFragment(fetchFolderDB.retrieveFolders(),requireContext())
        recyclerView.adapter = adapterClass
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_to_folder_sheet, container, false)
    }
}