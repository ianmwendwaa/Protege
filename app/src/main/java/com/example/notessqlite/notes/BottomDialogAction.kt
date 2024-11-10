package com.example.notessqlite.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notessqlite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomDialogAction:BottomSheetDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
    }
}