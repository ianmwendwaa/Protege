package com.example.notessqlite.relationships

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.database.RelationshipDatabase
import com.example.notessqlite.databinding.FragmentRelationshipsBinding

class RelationshipsFragment : Fragment() {
    private var _binding:FragmentRelationshipsBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRelationshipsBinding.inflate(inflater,container,false)
        return binding.root
    }

    private lateinit var relationshipAdapter: RelationshipAdapter
    private lateinit var db:RelationshipDatabase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.relationshipRV.layoutManager = LinearLayoutManager(context)
        db = context?.let { RelationshipDatabase(it) }!!
        relationshipAdapter = RelationshipAdapter(db.getAllRelationships(),requireContext())
        binding.relationshipRV.layoutManager = LinearLayoutManager(context)

        binding.relationshipRV.adapter = relationshipAdapter

        binding.addRelationship.setOnClickListener {
            startActivity(Intent(requireContext(), AddRelationshipActivity::class.java))
        }
    }
}