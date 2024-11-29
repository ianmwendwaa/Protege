package com.example.notessqlite.categories.categoryviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.CategoriesDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase

class ViewNotesInFolder:AppCompatActivity() {
//    Database components
    private lateinit var db:CategoriesDatabase
    private lateinit var driverDB: InsertNoteIntoFolderDatabase
    private var folderId = -1
//    xml and preview attributes
    private lateinit var viewNotesInFolderAdapter: ViewNotesInFolderAdapter
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
        viewNotesInFolderAdapter = ViewNotesInFolderAdapter(driverDB.getFoldersNotes(),this)
        recyclerView.adapter = viewNotesInFolderAdapter
        folderId = intent.getIntExtra("folder_Id",-1)
        if (folderId==-1){
            finish()
            return
        }

        val folder = db.getFolderById(folderId)

        folderName.setText(folder.folderName)
        val saveFolder = findViewById<Button>(R.id.saveFolder)
        saveFolder.setOnClickListener {
            
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        viewNotesInFolderAdapter.refreshData(driverDB.getFoldersNotes())
        viewNotesInFolderAdapter.notifyDataSetChanged()
    }
}