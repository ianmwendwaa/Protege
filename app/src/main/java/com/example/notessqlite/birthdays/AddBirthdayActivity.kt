package com.example.notessqlite.birthdays

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.notessqlite.R
import com.example.notessqlite.database.BirthDayDatabase
import com.example.notessqlite.toasts.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale

class AddBirthdayActivity : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_birthday,container,false)
    }

    private lateinit var db:BirthDayDatabase
    private lateinit var date:TextInputEditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = context?.let { BirthDayDatabase(it) }!!
        val birthdayName:TextInputEditText = view.findViewById(R.id.birthdayName)
        val birthDay:TextInputEditText = view.findViewById(R.id.birthdayDate)
        val tvSelected:TextView = view.findViewById(R.id.birthdaySelected)
        val saveButton:ImageView = view.findViewById(R.id.saveBirthdayBtn)
        birthdayName.requestFocus()
        tvSelected.setOnClickListener {
            showDatePicker()
        }
        saveButton.setOnClickListener {
            val name = birthdayName.text.toString()
            val dob = birthDay.text.toString()
            val birthday = Birthday(0,name, dob)
            db.createBirthday(birthday)
            Utils.showToast(requireContext(),"Protege will remember that!", R.drawable.ic_info)
            Utils.showToast(requireContext(),"Wishing $name on $dob!", R.drawable.birthday_toast)
            dismiss()
        }
    }
    private fun showDatePicker() {
        date = requireView().findViewById(R.id.birthdayDate)
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_,selectedYear,selectedMonth,selectedDay->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(
                    selectedYear,
                    selectedMonth,
                    selectedDay
                )
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)
                date.setText(formattedDate)
            },year,month,day
        )
        datePickerDialog.show()
    }
}