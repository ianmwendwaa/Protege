package com.example.notessqlite.todo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.notessqlite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

open class BottomSheetFragment : BottomSheetDialogFragment() {
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

        val inputManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(taskName, InputMethodManager.SHOW_IMPLICIT)

        createNotificationChannel()

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
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            Toast.makeText(context, "$title saved at $time", Toast.LENGTH_SHORT).show()
            Toast.makeText(context,"Reminder for $time", Toast.LENGTH_SHORT).show()
        }
    }

    //@SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminder() {
        val intent = Intent(this.context, NotificationManager::class.java)
        val title = view?.findViewById<TextInputEditText>(R.id.taskName)?.text.toString()
        val description = view?.findViewById<TextInputEditText>(R.id.taskDescription)?.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(contentExtra, description)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        if(alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
            showAlert(time, title, description)
        }else{
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }

    private fun showAlert(time: Long, title: String, description: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)
        AlertDialog.Builder(context)
            .setTitle("Protege Reminder")
            .setMessage("title: " + title +
                        "\ndescription: "+ description+
                        "\nAt: "+ dateFormat.format(date)+" " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_->}
            .show()
        Toast.makeText(context, "Reminder set for $timeFormat", Toast.LENGTH_SHORT).show()

    }

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private fun getTime(): Long {
        //val datePicked = view?.findViewById<DatePicker>(R.id.datePicker)
        //val timePicker = view?.findViewById<TimePicker>(R.id.timePicker)
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        val minute = timePicker.minute
        val hour = timePicker.hour
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, minute, hour)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Reminder Channel"
        val description = "Channel for reminder notifications"
        val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
        val channel = android.app.NotificationChannel(channelID, name, importance)
        channel.description = description
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
}
