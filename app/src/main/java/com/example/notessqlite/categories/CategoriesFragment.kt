package com.example.notessqlite.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notessqlite.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addCategory:FloatingActionButton = view.findViewById(R.id.categoryAddBtn)

        addCategory.setOnClickListener {
            fragmentManager.let { it1 ->
                if (it1 != null) {
                    CreateCategory().show(it1,"newCategory")
                }
            }
        }
    }
}