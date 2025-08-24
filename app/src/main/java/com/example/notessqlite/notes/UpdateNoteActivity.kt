@file:Suppress("UsePropertyAccessSyntax", "NAME_SHADOWING")

package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.notessqlite.R
import com.example.notessqlite.toasts.Utils
import com.example.notessqlite.database.NoteDatabase
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
        binding.updateDate.setText(note.time)
        binding.charCountUpdate.setText(note.charCounter)
        val currentDateTime = LocalDateTime.now()
        val newDate = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm"))
        binding.updateDate.text = newDate

        binding.updateContentEditText.requestFocus()

        binding.updateContentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                //  Word Counter
                fun countWords(contentEditText: EditText): Int {
                    val text = contentEditText.text.toString().trim()
                    return if (text.isEmpty()) {
                        0
                    } else {
                        text.split("\\s+".toRegex()).size
                    }
                }

                val editTxt: EditText = findViewById(R.id.updateContentEditText)
                val wordCount = countWords(editTxt)
                binding.updateWordCount.text = "Words: $wordCount"

                val currentLength = s?.length?:0
                val limit = 100000000
                binding.charCountUpdate.text = "Characters: $currentLength"
                if(currentLength>=limit){
                    val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(binding.updateContentEditText.applicationWindowToken, 0)
                    binding.updateContentEditText.clearFocus()
                }else return
            }
        })
        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val titleText = binding.updateTitleEditText.text.isNotEmpty()
                val contentText = binding.updateContentEditText.text.isNotEmpty()
                binding.updateSaveButton.isEnabled = titleText||contentText
                if(binding.updateSaveButton.isEnabled){
                    binding.updateSaveButton.setTextColor(ContextCompat.getColor(this@UpdateNoteActivity, R.color.buttonEnabledColor))
                }else{
                    binding.updateSaveButton.setTextColor(ContextCompat.getColor(this@UpdateNoteActivity, R.color.buttonDisabledColor))
                }
            }
        }
        binding.updateTitleEditText.addTextChangedListener(textWatcher)
        binding.updateContentEditText.addTextChangedListener(textWatcher)


        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val currentDateTime = LocalDateTime.now()
            val newDate = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            val updatedNote = Note(noteId, newTitle, newContent, newDate)
            val datePresentation = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm:ss"))
            db.updateNote(updatedNote)
            Utils.showToast(this,"Protege will remember that",R.drawable.ic_info)
            Utils.showToast(this,"$newTitle updated successfully at $datePresentation",R.drawable.toast_note_taken)
            finish()
        }
    }
}
