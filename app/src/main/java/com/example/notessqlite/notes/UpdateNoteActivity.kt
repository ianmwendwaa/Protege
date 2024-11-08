@file:Suppress("UsePropertyAccessSyntax", "NAME_SHADOWING")

package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databases.NoteDatabase
import com.example.notessqlite.databinding.ActivityUpdateBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NoteDatabase
    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabase(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.charCountUpdate.setText(note.charCounter)
        val currentDateTime = LocalDateTime.now()
        val newDate = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm"))
        binding.updateDate.text = newDate
        //binding.updateDate.setText(note.time)

        binding.updateContentEditText.requestFocus()

        binding.updateContentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length?:0
                val limit = 100000000
                binding.charCountUpdate.text = "$currentLength"
                if(currentLength>=limit){
                    val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(binding.updateContentEditText.applicationWindowToken, 0)
                    binding.updateContentEditText.clearFocus()
                }else return
            }
        })

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val currentDateTime = LocalDateTime.now()
            val newDate = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            val updatedNote = Note(noteId, newTitle, newContent, newDate)
            val datePresentation = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm:ss"))
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "$newTitle updated at $datePresentation", Toast.LENGTH_SHORT).show()
        }
    }
}
