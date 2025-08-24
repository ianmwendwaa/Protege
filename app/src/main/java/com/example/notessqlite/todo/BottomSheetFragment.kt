package com.example.notessqlite.todo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.notessqlite.R
import com.example.notessqlite.database.ToDoDatabase
import com.example.notessqlite.notes.NotificationReceiver
import com.example.notessqlite.toasts.Utils
import com.example.notessqlite.toasts.datePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetFragment(private val viewModel: ToDoViewModel) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    @SuppressLint("SetTextI18n")
    private lateinit var db: ToDoDatabase
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var datePicked: TextView
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission: ", "Granted")
        } else {
            Log.w("Permission: ", "Denied")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BottomSheetFragment", "onViewCreated() called")
        val taskName: TextInputEditText = view.findViewById(R.id.taskName)
        val saveBtn: ImageView = view.findViewById(R.id.savetoDoButton)
        datePicked = view.findViewById(R.id.tvSelected)
        db = requireContext().let { ToDoDatabase(it) }
        toDoAdapter = ToDoAdapter(db.getAllToDos(), requireContext(), viewModel)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        taskName.requestFocus()

        datePicked.setOnClickListener {
            Log.d("BottomSheetFragment", "datePicked.setOnClickListener() called")
            if (alarmManager.canScheduleExactAlarms()) {
                showDateTimePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        saveBtn.setOnClickListener {
            Log.d("BottomSheetFragment", "saveBtn.setOnClickListener() called")
            val title = taskName.text.toString().trim()
            val description =
                view.findViewById<TextInputEditText>(R.id.taskDescription)?.text.toString().trim()
            val time = datePicked.text.toString()
            val todo = ToDo(0, title, description, time)
            // scheduleReminder()
            viewModel.insertToDo(todo)
//            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            Utils.showToast(requireContext(), "Protege will remember that.", R.drawable.ic_info)
            Utils.showToast(
                requireContext(),
                "I shall remind you to $title at $time",
                R.drawable.butterfly_effect
            )
            dismiss()
        }
    }

    private fun showDateTimePicker() {
        Log.d("BottomSheetFragment", "showDateTimePicker() called")
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(
                            selectedYear,
                            selectedMonth,
                            selectedDay,
                            selectedHour,
                            selectedMinute,
                            0
                        )
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val formattedDateTime = dateFormat.format(selectedCalendar.time)
                        datePicked.text = formattedDateTime
                        scheduleNotification(selectedCalendar)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun scheduleNotification(calendar: Calendar) {
        Log.d("BottomSheetFragment", "scheduleNotification() called")
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val description =
            view?.findViewById<TextInputEditText>(R.id.taskDescription)?.text.toString().trim()
        intent.putExtra("message", description) // Use "message" as the key

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val timeInMillis = calendar.timeInMillis
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            Utils.showToast(requireContext(),"Notification set!",R.drawable.ic_info)
        }catch (e:Exception){
            Utils.showToast(requireContext(),"Permission denied!",R.drawable.ic_info)
            Log.e("Bottom sheet Fragment","Exception",e)
        }
    }
}
