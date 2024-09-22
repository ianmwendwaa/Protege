package com.example.notessqlite.todo

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.notessqlite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

open class BottomSheetFragment : BottomSheetDialogFragment() {
    private var hours = 0
    private var minutes = 0

    private var myHours: Int = 0
    private var myMinutes: Int = 0

    @SuppressLint("SetTextI18n")
    lateinit var db: ToDoDatabase
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datePicked: TextView = view.findViewById(R.id.tvSelected)
        val taskName: TextInputEditText = view.findViewById(R.id.taskName)
        val saveBtn: ImageView = view.findViewById(R.id.savetoDoButton)
        db = context?.let { it1 -> ToDoDatabase(it1) }!!

        taskName.requestFocus()

        val inputManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(taskName, InputMethodManager.SHOW_IMPLICIT)

        datePicked.setOnClickListener {
            val calendar = Calendar.getInstance()
            hours = calendar.get(Calendar.HOUR)
            minutes = calendar.get(Calendar.MINUTE)
            val formatter = SimpleDateFormat("MM/dd")
            val date = Date()
            val current = formatter.format(date)
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour:Int, minute:Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                myHours = hour
                myMinutes = minute
                if (myMinutes<10){
                    datePicked.text = "$current $myHours:0$myMinutes"
                }else{
                    datePicked.text = "$current  $myHours:$myMinutes"
                }

            }
            val timePickerDialog = TimePickerDialog(context, timeSetListener, hours, minutes, true)
            timePickerDialog.show()

        }
        saveBtn.setOnClickListener {
            val title = view.findViewById<TextInputEditText>(R.id.taskName)?.text.toString().trim()
            val description = view.findViewById<TextInputEditText>(R.id.taskDescription)?.text.toString().trim()
            val time = datePicked.text.toString()
            val todo = ToDo(0, title, description, time)
            db.insertToDo(todo)
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            Toast.makeText(context, "$title saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
}
