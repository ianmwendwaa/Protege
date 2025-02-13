package com.example.notessqlite.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.notessqlite.R
import com.example.notessqlite.databases.ArchivesDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class DialogActionPrompt:BottomSheetDialogFragment() {
    private lateinit var archivesDatabase: ArchivesDatabase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val archiveFunction: LinearLayout = view.findViewById(R.id.archiveThis)
//        val starThis:LinearLayout = view.findViewById(R.id.starNote)
        val addToFolder:LinearLayout = view.findViewById(R.id.addToFolder)
        archivesDatabase = context?.let { ArchivesDatabase(it) }!!

        archiveFunction.setOnClickListener {
//            archivesDatabase.insertArchivedNote()
        }
        addToFolder.setOnClickListener {
            fragmentManager?.let { it1 -> AddToFolderDialog().show(it1, "newTask") }
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
    }
}