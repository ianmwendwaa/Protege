package com.example.notessqlite.archives.ui.birthday

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.AddBirthdayActivity
import com.example.notessqlite.BirthdayAdapter
import com.example.notessqlite.database.BirthDayDatabase
import com.example.notessqlite.database.RelationshipDatabase
import com.example.notessqlite.databinding.FragmentBirthdayBinding
import com.example.notessqlite.folders.CategoryAdapter
import com.example.notessqlite.todo.BottomSheetFragment

class BirthdayFragment : Fragment() {

    private var _binding: FragmentBirthdayBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val slideshowViewModel =
//            ViewModelProvider(this).get(BirthdayViewModel::class.java)

        _binding = FragmentBirthdayBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var db:BirthDayDatabase
    private lateinit var adapter:BirthdayAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = context?.let { BirthDayDatabase(it) }!!
        adapter = BirthdayAdapter(db.getBirthdays(),requireContext())
        binding.birthdayRV.layoutManager = LinearLayoutManager(context)
        binding.birthdayRV.adapter = adapter
        binding.birthdayAddBtn.setOnClickListener {
            val bottomSheetFragment = AddBirthdayActivity()
            bottomSheetFragment.show(requireActivity().supportFragmentManager,bottomSheetFragment.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(db.getBirthdays())
    }
}