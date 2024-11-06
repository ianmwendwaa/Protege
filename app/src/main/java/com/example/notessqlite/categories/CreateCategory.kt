package com.example.notessqlite.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.notessqlite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateCategory:BottomSheetDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName:EditText = view.findViewById(R.id.categoryName)

        categoryName.requestFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheets, container, false)
    }
}