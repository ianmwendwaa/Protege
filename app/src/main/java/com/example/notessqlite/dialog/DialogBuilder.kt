package com.example.notessqlite.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.notessqlite.R

class DialogBuilder(private val listener: DialogButtonEventHandler, val dialogTitle: String, val dialogMessageInfo: String):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.custom_dialog_popup, null)

            //--------Inflating the layout-------------
            val dialogPopup = Dialog(requireContext())
            dialogPopup.setContentView(R.layout.custom_dialog_popup)

            val okButton = view?.findViewById<Button>(R.id.okButton)
            val cancelButton = view?.findViewById<Button>(R.id.cancelButton)

            val dialogTitleTextView = view?.findViewById<TextView>(R.id.dialogTitle)
            val dialogContentTextInfoTextView = view?.findViewById<TextView>(R.id.dialogContentInfoTxt)

            dialogTitleTextView?.text = dialogTitle
            dialogContentTextInfoTextView?.text = dialogMessageInfo

            okButton?.setOnClickListener{
                listener.okButtonOnClickListener()
                dismiss()
            }

            cancelButton?.setOnClickListener {
                listener.cancelButtonOnClickListener()
                dismiss()
            }
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    fun buildDialogMessage(dialogTitle:String, dialogMessageInfo: String){
        val dialogTitleTextView = view?.findViewById<TextView>(R.id.dialogTitle)
        val dialogContentTextViewInfo = view?.findViewById<TextView>(R.id.dialogContentInfoTxt)
        dialogTitleTextView?.text = dialogTitle
        dialogContentTextViewInfo?.text = dialogMessageInfo
    }
}