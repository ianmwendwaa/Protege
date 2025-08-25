package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.notessqlite.LoadingActivity
import com.example.notessqlite.R
import com.example.notessqlite.R.drawable.ic_info
import com.example.notessqlite.database.NoteDatabase
import com.example.notessqlite.toasts.CodeBase
import com.example.notessqlite.toasts.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNoteActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var tvDate: TextView
    private lateinit var saveButton: Button
    private lateinit var charCount: TextView
    private lateinit var wordCounter: TextView
    private var animationTrigger:AnimationTrigger?=null
    private var isBullets = false
    private var isItalicsEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        db = NoteDatabase(this)
        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        tvDate = findViewById(R.id.tvDate)
        charCount = findViewById(R.id.charCount)
        wordCounter = findViewById(R.id.wordCount)
        saveButton = findViewById(R.id.saveButton)

        titleEditText.requestFocus()

//        Setting time for the date view
        val currentDateTime = LocalDateTime.now()
        val time = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        tvDate.text = time


        val wordCounterInit = "Words: 0"
        val charCounterInit = "Characters: 0"
        wordCounter.text = wordCounterInit
        charCount.text = charCounterInit
        contentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("SetTextI18n")
//                Text Watcher to get words and characters respectively
            override fun afterTextChanged(s: Editable?) {
                //                Word Counter
                fun countWords(contentEditText: EditText): Int {
                    val text = contentEditText.text.toString().trim()
                    return if (text.isEmpty()) {
                        0
                    } else {
                        text.split("\\s+".toRegex()).size
                    }
                }

                val editTxt: EditText = findViewById(R.id.contentEditText)
                val wordCount = countWords(editTxt)
                wordCounter.text = "Words: $wordCount"

//                Character counter
                val currentLength = s?.length ?: 0
                val limit = 100000000
                charCount.text = "Characters: $currentLength"
                if (currentLength >= limit) {
                    val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(contentEditText.applicationWindowToken, 0)
                }
            }
        })
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (isBullets && count > 0 && s?.get(start + count - 1) == '\n') {
                        val spannable = SpannableStringBuilder(s)
                        spannable.insert(start + count, "• ")
                        contentEditText.text = spannable
                        contentEditText.setSelection(start + count + 2)
                    }
                } catch (e: Exception) {
                    Log.e("BulletsButton", "Error toggling", e)
                }
                if (isItalicsEnabled && count > 0) {
                    val spannable = SpannableStringBuilder(s)
                    spannable.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        start + count,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    contentEditText.text = spannable
                }
            }

            //          Disabling the save Button until all fields are filled
            override fun afterTextChanged(s: Editable?) {
                val titleText = titleEditText.text.isNotEmpty()
                val contentText = contentEditText.text.isNotEmpty()
                saveButton.isEnabled = titleText && contentText
                if (saveButton.isEnabled) {
                    saveButton.setTextColor(
                        ContextCompat.getColor(
                            this@AddNoteActivity,
                            R.color.buttonEnabledColor
                        )
                    )
                } else {
                    saveButton.setTextColor(
                        ContextCompat.getColor(
                            this@AddNoteActivity,
                            R.color.buttonDisabledColor
                        )
                    )
                }
            }
        }
        titleEditText.addTextChangedListener(textWatcher)
        contentEditText.addTextChangedListener(textWatcher)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val charCount = charCount.text.toString()
            val savedDateTime = LocalDateTime.now()
            val savedTime = savedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            tvDate.text = savedTime
            val datePresentation = savedDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val note = Note(0, title, content, savedTime, charCount)
            if(title.isEmpty() && content.isEmpty()){
                return@setOnClickListener
            }else{
                db.insertNote(note)
                finish()
                startActivity(Intent(this, LoadingActivity::class.java))

                Handler(Looper.getMainLooper()).postDelayed({
                    Utils.showToast(this, "$title saved successfully at $datePresentation", R.drawable.toast_note_taken)
                },5000)
            }
        }
    }
}