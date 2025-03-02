package com.example.notessqlite.toasts

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
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

@Suppress("DEPRECATION")
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
            toast.duration = Toast.LENGTH_LONG
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