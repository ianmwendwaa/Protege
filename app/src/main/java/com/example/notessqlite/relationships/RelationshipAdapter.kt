package com.example.notessqlite.relationships

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.CodeBase
import com.example.notessqlite.R
import com.example.notessqlite.databases.RelationshipDatabase

class RelationshipAdapter(private var relationships: MutableList<Relationship>, context: Context) : RecyclerView.Adapter<RelationshipAdapter.RelationshipViewHolder>() {

    private val db: RelationshipDatabase = RelationshipDatabase(context)

    class RelationshipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.relationshipStatus)
        val card: LinearLayout = itemView.findViewById(R.id.what)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelationshipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.relationship_rv_item, parent, false)
        return RelationshipViewHolder(view)
    }

    override fun getItemCount(): Int = relationships.size

    override fun onBindViewHolder(holder: RelationshipViewHolder, position: Int) {
        val relationship = relationships[position]
        holder.titleTextView.text = relationship.name
        holder.contentTextView.text = relationship.relationshipStatus

        holder.card.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateRelationshipActivity::class.java).apply {
                putExtra("note_id", relationship.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            val title = relationship.name
            relationship.id.let { it1 -> db.deleteRelationship(it1) }
            refreshData(db.getAllRelationships())
            CodeBase.showToast(
                holder.itemView.context,
                "This action will have consequences.",
                R.drawable.butterfly_effect
            )
            CodeBase.showToast(holder.itemView.context, "$title deleted", R.drawable.ic_info)
        }
        val randomColor = getRandomColor()
        val borderDrawable = GradientDrawable()
        borderDrawable.setStroke(3,randomColor)
        borderDrawable.cornerRadius = 10f
//        holder.cardView.background = borderDrawable
    }

    private fun getRandomColor(): Int {
        val random = java.util.Random()
        return Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: MutableList<Relationship>) {
        relationships =  newNotes
        notifyDataSetChanged()
    }
//    To be used in updating searchView
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList:List<Relationship>){
        relationships.clear()
        relationships.addAll(newList)
        notifyDataSetChanged()
    }
}