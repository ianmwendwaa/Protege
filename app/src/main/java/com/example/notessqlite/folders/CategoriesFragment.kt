package com.example.notessqlite.folders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.database.CategoriesDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class CategoriesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var db:CategoriesDatabase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addCategory:FloatingActionButton = view.findViewById(R.id.categoryAddBtn)
        val recyclerView:RecyclerView = view.findViewById(R.id.categoryRV)
        val noCategories:LinearLayout = view.findViewById(R.id.noCategory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { CategoriesDatabase(it) }!!
        categoryAdapter = CategoryAdapter(db.retrieveFolders(), requireContext())
        recyclerView.adapter = categoryAdapter
        val nullCategoryQueryListResult = db.retrieveFolders()
        if (nullCategoryQueryListResult.isEmpty()){
            noCategories.visibility = View.VISIBLE
        }else{
            noCategories.visibility = View.GONE
        }
        addCategory.setOnClickListener {
            fragmentManager?.let { it1 ->
                CreateCategory().show(it1,"newCategory")
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val noCategories:LinearLayout = requireActivity().findViewById(R.id.noCategory)
        val query = db.retrieveFolders()
        if (query.isEmpty()){
            noCategories.visibility = View.VISIBLE
        }else{
            noCategories.visibility = View.GONE
        }
        categoryAdapter.refreshData(db.retrieveFolders())
    }
}