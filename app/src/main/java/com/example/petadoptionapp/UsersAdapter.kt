package com.example.petadoptionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter (private val users: List<UserData>) :
    RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userLetterBtn: Button? = null
        var emailView: TextView? = null
        var interestView : TextView? = null

        init {
            userLetterBtn = itemView.findViewById(R.id.userLetterBtn)
            emailView = itemView.findViewById(R.id.emailView)
            interestView = itemView.findViewById(R.id.interestView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.emailView?.text = users[position].email

        if (users[position].reservationsAt.isNullOrEmpty())
            holder.interestView?.text = "Not interested in pet yet"
        else
            holder.interestView?.text = "Interested in: ${users[position].reservationsAt}"

        holder.userLetterBtn?.text = users[position].email[0].toString()
        holder.userLetterBtn?.textSize = 30F
    }

    override fun getItemCount(): Int {
        return users.size
    }
}