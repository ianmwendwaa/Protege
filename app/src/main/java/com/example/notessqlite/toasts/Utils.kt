package com.example.notessqlite.toasts

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.icu.util.Calendar
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.notessqlite.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@Suppress("DEPRECATION", "unused")
class Utils {
    companion object{
        fun showToast(context: Context, message:String,image:Int){
            val inflater = LayoutInflater.from(context)
            val layout:View = inflater.inflate(R.layout.toast_message,null)

            val toastTextView = layout.findViewById<TextView>(R.id.toastMessage)
            val fontFamily:Typeface?= ResourcesCompat.getFont(context, R.font.indie_flower)

            val toastImageView = layout.findViewById<ImageView>(R.id.toast_image)
            toastImageView.setImageResource(image)

            toastTextView.typeface = fontFamily
            toastTextView.text = message

            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        }
        fun applyBoldToText(editText: EditText){
            val start = editText.selectionStart
            val end = editText.selectionEnd
            if(start!=end){
                val spannable = SpannableStringBuilder(editText.text)
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                editText.text = spannable
            }
        }
        fun applyUnderlineToText(editText: EditText){
            val start = editText.selectionStart
            val end = editText.selectionEnd
            if(start!=end){
                val spannable = SpannableStringBuilder(editText.text)
                spannable.setSpan(
                    UnderlineSpan(),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                editText.text = spannable
            }
        }
        fun displayBirthday(txtView:TextView, birthday:String){
            val currentDay = LocalDate.now()
            val month = Calendar.getInstance().get(Calendar.MONTH)+1
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        }

        fun applyItalicToText(editText: EditText){
            val start = editText.selectionStart
            val end = editText.selectionEnd
            if(start!=end){
                val spannable = SpannableStringBuilder(editText.text)
                spannable.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                editText.text = spannable
            }
        }


//        fun launchImagePicker(editText: EditText){
//
//        }
    }
    class ResizableImageSpan(context: Context, uri: Uri, private val width: Int, private val height: Int) : ImageSpan(context, uri) {

        override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
            val d = drawable
            val rect = Rect(0, 0, width, height)
            d.bounds = rect
            if (fm != null) {
                fm.ascent = -height
                fm.descent = 0

                fm.top = fm.ascent
                fm.bottom = 0
            }
            return width
        }
    }
}
@Suppress("UnusedVariable", "unused")
class datePickerDialog{
    companion object{
        fun showDatePickerDialog(context: Context,datePicked:TextInputEditText){
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val inflater = LayoutInflater.from(context)
            val layout:View = inflater.inflate(R.layout.activity_add_birthday,null)

            val datePickerDialog = DatePickerDialog(
                context,
                {_,selectedYear,selectedMonth,selectedDay->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(
                        selectedYear,
                        selectedMonth,
                        selectedDay
                    )
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedCalendar.time)
//                    datePicked.text = formattedDate
                },year,month,day
            )
            datePickerDialog.show()
        }
    }
}