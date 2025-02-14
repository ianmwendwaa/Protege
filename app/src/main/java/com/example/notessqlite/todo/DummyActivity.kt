package com.example.notessqlite.todo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.R
import com.example.notessqlite.Utils
import com.example.notessqlite.databases.ToDoDatabase
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class DummyActivity:AppCompatActivity(),TimePickerDialog.OnTimeSetListener {
    @SuppressLint("SetTextI18n")
    var hours = 0
    private var minutes = 0

    private var myHours: Int = 0
    private var myMinutes: Int = 0
    private lateinit var db: ToDoDatabase
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var taskName:TextInputEditText
    private lateinit var taskDescription:TextInputEditText
    private lateinit var saveBtn:ImageView
    private lateinit var datePicked:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dummy_tester)
        taskName = findViewById(R.id.name)
        taskDescription = findViewById(R.id.desc)
        saveBtn = findViewById(R.id.savetoDoButtonz)
        datePicked = findViewById(R.id.tvSelectedz)
        db = ToDoDatabase(this)
        toDoAdapter = ToDoAdapter(db.getAllToDos(),this)

        taskName.requestFocus()

        datePicked.setOnClickListener {
            val calendar = Calendar.getInstance()
            hours = calendar.get(Calendar.HOUR)
            minutes = calendar.get(Calendar.MINUTE)
//            val formatter = SimpleDateFormat("MM/dd")
//            val date = Date()
//            val current = formatter.format(date)
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour:Int, minute:Int ->
//                calendar.set(Calendar.HOUR_OF_DAY, hour)
//                calendar.set(Calendar.MINUTE, minute)
//                myHours = hour
//                myMinutes = minute
//                if (myMinutes<10){
//                    datePicked.text = "$current $myHours:0$myMinutes"
//                }else{
//                    datePicked.text = "$current  $myHours:$myMinutes"
//                }
            val timePickerDialog = TimePickerDialog(this, this, hours, minutes, true)
            timePickerDialog.show()
        }
        saveBtn.setOnClickListener {
            val title = taskName.text.toString().trim()
            val description = taskDescription.text.toString().trim()
            val time = datePicked.text.toString()
            val todo = ToDo(0, title, description,time)
            // scheduleReminder()
            toDoAdapter.refreshData(db.getAllToDos())
            db.insertToDo(todo)
            setAlarm()
//            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            Utils.showToast(this,"Protege will remember that.", R.drawable.ic_info)
            Utils.showToast(this, "I shall remind you to $title at $time", R.drawable.butterfly_effect)
            finish()
        }
    }
    private fun setAlarm() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,NotificationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to trigger at the specified time
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

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHours = hourOfDay
        myMinutes = minute
        val time = "$myHours:$myMinutes"
        datePicked.text = time
    }
}