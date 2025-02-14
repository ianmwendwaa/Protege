package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.notessqlite.R
import com.example.notessqlite.R.drawable.ic_info
import com.example.notessqlite.Utils
import com.example.notessqlite.databases.NoteDatabase
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
//    private lateinit var toolBar: LinearLayout
//    private lateinit var boldButton: Button
//    private lateinit var italicButton: Button
//    private lateinit var underline: LinearLayout
//    private lateinit var bullets: LinearLayout
//    private lateinit var voiceRecorder: LinearLayout
//    private lateinit var attachment: LinearLayout

    private lateinit var rootView: ScrollView
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
//        passwordMagic =findViewById(R.id.passwordSetter)
        saveButton = findViewById(R.id.saveButton)
//        toolBar = findViewById(R.id.toolbar)
//        boldButton = findViewById(R.id.boldButton)
//        italicButton = findViewById(R.id.italicButton)
//        underline = findViewById(R.id.underline)
//        bullets = findViewById(R.id.bullets)
//        attachment = findViewById(R.id.attachment)
//        voiceRecorder = findViewById(R.id.voiceRecorder)
//        rootView = findViewById(R.id.main)
        titleEditText.requestFocus()

//        Setting time for the date view
        val currentDateTime = LocalDateTime.now()
        val time = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        tvDate.text = time

////        Setting the toolbar to go with the flow of the keyboard
//        rootView.viewTreeObserver.addOnGlobalLayoutListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.rootView.height
//            val keyboardHeight = screenHeight - rect.bottom
////            if(keyboardHeight > 0){
////                toolBar.translationY = -keyboardHeight.toFloat()
////            }else{
////                toolBar.translationY = 0f
////            }
//        }
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


        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageUri = data?.data
                    if (imageUri != null) {
                        val imageSpan = Utils.ResizableImageSpan(this, imageUri, 500, 700)
                        val editable = contentEditText.editableText
                        editable.insert(contentEditText.selectionStart, " ")
                        editable.setSpan(
                            imageSpan,
                            editable.length - 1,
                            editable.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
//        boldButton.setOnClickListener {
//            applyBoldToText(contentEditText)
//        }
//        italicButton.setOnClickListener {
//            applyItalicToText(contentEditText)
//        }
//        underline.setOnClickListener {
//            applyUnderlineToText(contentEditText)
//        }
//        bullets.setOnClickListener {
//            Toast.makeText(this,"Still in development; Should display bullets..",Toast.LENGTH_LONG).show()
//        }
//        attachment.setOnClickListener {
//            val pickImageIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            pickImageLauncher.launch(pickImageIntent)
//        }
//        voiceRecorder.setOnClickListener {
//            Toast.makeText(this,"Still in development; Should record voice notes..",Toast.LENGTH_LONG).show()
//        }
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val charCount = charCount.text.toString()
            val savedDateTime = LocalDateTime.now()
            val savedTime = savedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            tvDate.text = savedTime
            val datePresentation = savedDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val note = Note(0, title, content, savedTime, charCount)
            db.insertNote(note)
            animationTrigger?.triggerAnimation()
            Utils.showToast(this, "Protege will remember that!",ic_info)
            Utils.showToast(this, "$title saved successfully at $datePresentation",
                R.drawable.toast_note_taken
            )
            finish()
//            findNavController().navigate(
//                R.id.idTVDate, bundleOf("newNotePosition" to newNotePosition)
//            )
        }
    }
}