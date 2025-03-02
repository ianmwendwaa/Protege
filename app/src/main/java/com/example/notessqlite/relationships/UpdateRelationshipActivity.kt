package com.example.notessqlite.relationships

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.toasts.CodeBase
import com.example.notessqlite.R
import com.example.notessqlite.database.RelationshipDatabase
import com.example.notessqlite.databinding.ActivityUpdateRelationshipBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateRelationshipActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateRelationshipBinding
    private lateinit var db:RelationshipDatabase
    private var relationshipId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRelationshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RelationshipDatabase(this)
        relationshipId = intent.getIntExtra("relationship_id",-1)
        if(relationshipId == -1){
            finish()
            return
        }

        val relationship = db.getRelationshipById(relationshipId)

        binding.updaterelationshipName.setText(relationship.name)
        binding.updaterelationshipStatus.setText(relationship.relationshipStatus)
        binding.updaterelationshipInfo.setText(relationship.moreInfo)

        val time = LocalDateTime.now()
        val standardisedNewDateTime = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        binding.updatetvDateToday.text = standardisedNewDateTime

        binding.updaterelationshipName.requestFocus()

        binding.statusHint.setOnClickListener {
            startActivity(Intent(this,RelationshipStatusHintActivity::class.java))
        }

        binding.updatesaveRelationship.setOnClickListener {
            val updateName = binding.updaterelationshipName.text.toString()
            var updateStatus = binding.updaterelationshipStatus.text.toString()
            val updateInfo = binding.updaterelationshipInfo.text.toString()

            val detroitInnit = updateStatus.toInt()
            when(detroitInnit){
                in 1..3 -> updateStatus = "Distant"
                in 4..5 -> updateStatus = "Neutral"
                in 6..8 -> updateStatus = "Ally"
                in 9..10 -> updateStatus = "Companion"
            }
            val relationShip = Relationship(0,updateName,updateStatus,updateInfo)
            db.updateRelationship(relationShip)
            CodeBase.showToast(this,"Relationship with $updateName updated!: $updateStatus", R.drawable.butterfly_effect)
            finish()
        }
    }
}