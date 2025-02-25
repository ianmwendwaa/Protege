package com.example.notessqlite.relationships

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.RelationshipDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RelationshipsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relationships, container, false)
    }

    private lateinit var relationshipAdapter: RelationshipAdapter
    private lateinit var db:RelationshipDatabase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView:RecyclerView = view.findViewById(R.id.relationshipRV)
        val addNewRelationshipFAB:FloatingActionButton = view.findViewById(R.id.addRelationship)

        db = context?.let { RelationshipDatabase(it) }!!
        relationshipAdapter = RelationshipAdapter(db.getAllRelationships(),requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = relationshipAdapter


        addNewRelationshipFAB.setOnClickListener {
            startActivity(Intent(requireContext(), AddRelationshipActivity::class.java))
        }

    }
}