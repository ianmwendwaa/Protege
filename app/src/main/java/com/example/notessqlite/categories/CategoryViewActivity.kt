package com.example.notessqlite.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.CategoriesDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase

class CategoryViewActivity:AppCompatActivity() {
//    Database components
    private lateinit var db:CategoriesDatabase
    private lateinit var driverDB: InsertNoteIntoFolderDatabase
    private var folderId = -1
//    xml and preview attributes
    private lateinit var categoryViewAdapter: CategoryViewAdapter
    private lateinit var folderName:EditText
    private lateinit var folderDescription:EditText
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_notes_in_category)
        db = CategoriesDatabase(this)
        folderName = findViewById(R.id.folderTitle)
        folderDescription = findViewById(R.id.folderDescription)
        recyclerView = findViewById(R.id.notesInCatRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        driverDB = InsertNoteIntoFolderDatabase(this)
        categoryViewAdapter = CategoryViewAdapter(driverDB.getFoldersNotes(),this)
        recyclerView.adapter = categoryViewAdapter
        folderId = intent.getIntExtra("folder_Id",-1)
        if (folderId==-1){
            finish()
            return
        }

        val folder = db.getFolderById(folderId)

        folderName.setText(folder.folderName)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        categoryViewAdapter.refreshData(driverDB.getFoldersNotes())
        categoryViewAdapter.notifyDataSetChanged()
    }
}