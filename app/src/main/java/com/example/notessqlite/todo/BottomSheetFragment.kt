package com.example.notessqlite.todo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.notessqlite.R
import com.example.notessqlite.databases.ToDoDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

open class BottomSheetFragment : BottomSheetDialogFragment(){
    @SuppressLint("SetTextI18n")
    private var hours = 0
    private var minutes = 0

    private var myHours: Int = 0
    private var myMinutes: Int = 0
    private lateinit var db: ToDoDatabase
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskName: TextInputEditText = view.findViewById(R.id.taskName)
        val saveBtn: ImageView = view.findViewById(R.id.savetoDoButton)
        val datePicked: TextView = view.findViewById(R.id.tvSelected)
        db = context?.let { it1 -> ToDoDatabase(it1) }!!

        taskName.requestFocus()

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
            val todo = ToDo(0, title, description,time)
           // scheduleReminder()
            db.insertToDo(todo)
            setAlarm()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            Toast.makeText(context, "$title saved at $time", Toast.LENGTH_SHORT).show()
            Toast.makeText(context,"Reminder for $time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlarm() {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, myHours)
        calendar.set(Calendar.MINUTE, myMinutes)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
}
