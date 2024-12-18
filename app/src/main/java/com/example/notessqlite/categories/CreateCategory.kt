package com.example.notessqlite.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.notessqlite.R
import com.example.notessqlite.databases.CategoriesDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateCategory:BottomSheetDialogFragment() {
    private lateinit var db:CategoriesDatabase
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName:EditText = view.findViewById(R.id.categoryName)
        val dateModification:TextView = view.findViewById(R.id.dateModification)
        val saveButton:ImageView = view.findViewById(R.id.saveCategory)
        db = context?.let { CategoriesDatabase(it) }!!
        categoryAdapter = CategoryAdapter(db.retrieveFolders(),requireContext())
        categoryName.requestFocus()
        val time = LocalDateTime.now()
        dateModification.text = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        saveButton.setOnClickListener {
            val folderTitle = categoryName.text.toString().trim()
            val time = LocalDateTime.now()
            val dateModified = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            dateModification.text = dateModified
            val category = Category(0,folderTitle,dateModified)
            db.createFolder(category)
            Toast.makeText(context,"$folderTitle folder created!",Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheets, container, false)
    }

    override fun onResume() {
        super.onResume()
        categoryAdapter.refreshData(db.retrieveFolders())
    }
}