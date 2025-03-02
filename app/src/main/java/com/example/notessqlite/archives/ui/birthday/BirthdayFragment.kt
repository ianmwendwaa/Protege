package com.example.notessqlite.archives.ui.birthday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notessqlite.database.BirthDayDatabase
import com.example.notessqlite.databinding.FragmentBirthdayBinding

class BirthdayFragment : Fragment() {

    private var _binding: FragmentBirthdayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}