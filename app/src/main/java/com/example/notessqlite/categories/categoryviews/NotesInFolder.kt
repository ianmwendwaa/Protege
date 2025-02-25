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
import com.example.notessqlite.databases.ArchivesDatabase
import com.example.notessqlite.databases.CategoriesDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotesInFolder:AppCompatActivity() {
    //    Database components
    private lateinit var db:CategoriesDatabase
    private lateinit var archivesDB: ArchivesDatabase
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
        setContentView(R.layout.activity_view_notes_in_general_folder)
        db = CategoriesDatabase(this)
        folderName = findViewById(R.id.folderTitle)
        folderDescription = findViewById(R.id.folderDescription)
        recyclerView = findViewById(R.id.notesInCatRV)
        date = findViewById(R.id.dateOfFolder)
        recyclerView.layoutManager = LinearLayoutManager(this)
        archivesDB = ArchivesDatabase(this)
        viewNotesInFolderAdapter = ViewNotesInFolderAdapter(archivesDB.getArchivedNotes(),this)
        recyclerView.adapter = viewNotesInFolderAdapter
        folderId = intent.getIntExtra("folder_Id",-1)
        if (folderId==-1){
            finish()
            return
        }

        val time = LocalDateTime.now()
        val standardDateTime = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        date.text = standardDateTime

        val folder = db.getFolderById(folderId)

        folderName.setText(folder.folderName)
        folderDescription.setText(folder.folderDescription)

        val saveFolder = findViewById<Button>(R.id.saveFolder)

        saveFolder.setOnClickListener {
            val title = folderName.text.toString()
            val description = folderDescription.text.toString()
            val savedDateTime = LocalDateTime.now()
            val savedTime = savedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            date.text = savedTime
            val folderData = Category(folderId,title,savedTime,description)
            db.updateNote(folderData)
            Utils.showToast(this,"Folder updated!",R.drawable.ic_info)
            finish()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        viewNotesInFolderAdapter.refreshData(archivesDB.getArchivedNotes())
        viewNotesInFolderAdapter.notifyDataSetChanged()
    }
}