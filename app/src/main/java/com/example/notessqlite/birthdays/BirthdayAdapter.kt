package com.example.notessqlite.birthdays

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.database.BirthDayDatabase
import com.example.notessqlite.toasts.CodeBase

class BirthdayAdapter(private var birthday: MutableList<Birthday>,context: Context):RecyclerView.Adapter<BirthdayAdapter.BirthdayViewHolder>() {
    private var db:BirthDayDatabase = BirthDayDatabase(context)
    class BirthdayViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val birthdayPersona: TextView = itemView.findViewById(R.id.folderName)
        val dob:TextView = itemView.findViewById(R.id.dateModifiedzz)
        val deleteButton:ImageView = itemView.findViewById(R.id.deleteFolder)
        val card:LinearLayout = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item,parent,false)
        return BirthdayViewHolder(view)
    }

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        val birthday = birthday[position]
        val birthdayPersona = birthday.name
        val dob = birthday.dob
        holder.birthdayPersona.text = birthdayPersona
        holder.dob.text = dob

        holder.card.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context,UpdateBirthdayActivity::class.java)
            )
        }
        holder.deleteButton.setOnClickListener {
            birthday.id.let {
                it1->db.deleteBirthday(it1)
            }
            CodeBase.showToast(holder.itemView.context,"Wrong entry?", R.drawable.butterfly_effect)
        }
    }

    override fun getItemCount(): Int = birthday.size

    fun refreshData(newBirthdays: MutableList<Birthday>) {
        birthday = newBirthdays
        notifyDataSetChanged()
    }
}