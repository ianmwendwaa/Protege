package com.example.notessqlite.categories.categoryviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.Utils
import com.example.notessqlite.categories.Category
import com.example.notessqlite.databases.CategoriesDatabase
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewNotesInFolder:AppCompatActivity() {
//    Database components
    private lateinit var db:CategoriesDatabase
    private lateinit var driverDB: InsertNoteIntoFolderDatabase
    private var folderId = -1
//    xml and preview attributes
    private lateinit var viewNotesInFolderAdapter: ViewNotesInFolderAdapter
    private lateinit var folderName:EditText
    private lateinit var folderDescription:EditText
    private lateinit var date:TextView
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_notes_in_category)
        db = CategoriesDatabase(this)
        folderName = findViewById(R.id.folderTitle)
        folderDescription = findViewById(R.id.folderDescription)
        recyclerView = findViewById(R.id.notesInCatRV)
        date = findViewById(R.id.dateOfFolder)
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
        folderDescription.setText(folder.dateModified)

        val savedDateTime = LocalDateTime.now()
        val savedTime = savedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        date.text = savedTime
        val saveFolder = findViewById<Button>(R.id.saveFolder)
        saveFolder.setOnClickListener {
            val title = folderName.text.toString()
            val description = folderDescription.text.toString()
            val dateModification = date.text.toString()
            val folderData = Category(folderId,title,dateModification,description)
            db.updateNote(folderData)
            Utils.showToast(this,"Testing",R.drawable.ic_info)
            finish()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        viewNotesInFolderAdapter.refreshData(driverDB.getFoldersNotes())
        viewNotesInFolderAdapter.notifyDataSetChanged()
    }
}