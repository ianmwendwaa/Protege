package com.example.notessqlite.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.notessqlite.R
import com.example.notessqlite.toasts.Utils
import com.example.notessqlite.database.CategoriesDatabase
import com.example.notessqlite.database.EntryDatabase
import com.example.notessqlite.toasts.CodeBase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateCategory:BottomSheetDialogFragment() {
    private lateinit var db:CategoriesDatabase
    private lateinit var entryDB:EntryDatabase
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName:EditText = view.findViewById(R.id.categoryName)
        val categoryDescription:EditText = view.findViewById(R.id.categoryDescription)
        val dateModification:TextView = view.findViewById(R.id.dateModification)
        val saveButton:ImageView = view.findViewById(R.id.saveCategory)
        db = context?.let { CategoriesDatabase(it) }!!
        entryDB = EntryDatabase(requireContext())
        categoryAdapter = CategoryAdapter(db.retrieveFolders(),requireContext())
        categoryName.requestFocus()
        val time = LocalDateTime.now()
        dateModification.text = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))

        saveButton.setOnClickListener {
            val folderTitle = categoryName.text.toString().trim()
            val folderDescription = categoryDescription.text.toString()
            val timeAtm = LocalDateTime.now()
            val dateModified = timeAtm.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            dateModification.text = dateModified
            if(folderTitle.isEmpty() && folderDescription.isEmpty()){
                CodeBase.showToast(context, "Fill in the name and description!", R.drawable.ic_info)
            }else{
                val category = Category(0,folderTitle,dateModified,folderDescription)
                db.createFolder(category)
                Utils.showToast(requireContext(),"New folder created!",R.drawable.butterfly_effect)
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
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