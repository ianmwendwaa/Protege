package com.example.notessqlite.relationships

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.CodeBase
import com.example.notessqlite.R
import com.example.notessqlite.databases.RelationshipDatabase
import com.example.notessqlite.databinding.ActivityAddRelationshipBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddRelationshipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRelationshipBinding
    private lateinit var db:RelationshipDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRelationshipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = RelationshipDatabase(this)

        binding.relationshipName.requestFocus()

        val date = LocalDateTime.now()
        val standardised = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        binding.tvDateToday.text = standardised

        binding.saveRelationship.setOnClickListener {
            val name = binding.relationshipName.text.toString()
            var status = binding.relationshipStatus.text.toString()
            val info = binding.relationshipInfo.text.toString()

            val detroitInnit = status.toInt()
            when(detroitInnit){
               in 1..3->
                   status = "Distant"
                in 4..5->
                    status = "Neutral"
                in 6..8->
                    status = "Ally"
                in 9..10->
                    status = "Companion"
            }
            val relationship = Relationship(0,name,status,info)
            db.createRelationship(relationship)
            finish()
        }
    }
}