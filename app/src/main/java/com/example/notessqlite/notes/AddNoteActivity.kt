package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.R
import com.example.notessqlite.databases.NoteDatabase
import com.example.notessqlite.user_passwords.PasswordActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNoteActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var tvDate: TextView
    private lateinit var saveButton: ImageView
    private lateinit var charCount: TextView
    private lateinit var bullets: ImageView
   // private lateinit var toolBox: LinearLayout
    private lateinit var passwordMagic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        db = NoteDatabase(this)
        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        tvDate = findViewById(R.id.tvDate)
        charCount = findViewById(R.id.charCount)
        charCount.text = 0.toString()
        bullets = findViewById(R.id.bullets)
        passwordMagic =findViewById(R.id.passwordSetter)
        saveButton = findViewById(R.id.saveButton)
       // toolBox = findViewById(R.id.toolBox)
        titleEditText.requestFocus()
        val currentDateTime = LocalDateTime.now()
        val time = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm"))
        tvDate.text = time
        contentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
//                if(s?.endsWith("\n")==true){
//                    val bullet = "\u2022"
//                    s.replace(s.length-1,s.length,"\n$bullet")
//                }
                val currentLength = s?.length?:0
                val limit = 100000000
                charCount.text = "$currentLength"
                if(currentLength>=limit){
                    val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(contentEditText.applicationWindowToken, 0)
                }
            }
        })
        passwordMagic.setOnClickListener {
            startActivity(Intent(this,PasswordActivity::class.java))
        }
//        if (title.isEmpty()&&content.isEmpty()){
//            saveButton.visibility = View.GONE
//        }else{
//            saveButton.visibility =
//        }
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val charCount = charCount.text.toString()
            val currentDateTime = LocalDateTime.now()
            val time = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            tvDate.text = time
            val datePresentation = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val note = Note(0, title, content,time,charCount)
            if(title.isEmpty()||content.isEmpty()){
                Toast.makeText(this, "Type something for me to do my thing!", Toast.LENGTH_SHORT).show()
            }else{
                db.insertNote(note)
                finish()
                Toast.makeText(this, "$title saved successfully at $datePresentation", Toast.LENGTH_SHORT).show()
            }
        }
    }
}