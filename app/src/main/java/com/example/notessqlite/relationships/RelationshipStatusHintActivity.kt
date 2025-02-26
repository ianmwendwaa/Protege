package com.example.notessqlite.relationships

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.R

class RelationshipStatusHintActivity:AppCompatActivity() {
    private lateinit var hintTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_hint)
        hintTextView = findViewById(R.id.relationshipHint)

        val info = "Relationship statuses range from 1-10 as follows. \n" +
                "1-3 -> This is someone whom you have not spoken to in quite a while. Most likely if " +
                "you drifted.\n\n" +
                "4-5 -> This is someone whom you'd just consider as an acquaintance. They are considered " +
                "neutral in this app.\n\n" +
                "6-8-> This is someone who is quite close to you. You associate with them more often" +
                "and are considered as an ally.\n\n" +
                "9-10 -> Now this is a really close person. It may be family, or your best friend. This " +
                "one is considered as a companion.\n\n"
        hintTextView.text = info
    }
}