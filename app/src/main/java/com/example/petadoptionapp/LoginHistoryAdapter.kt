package com.example.petadoptionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class LoginHistoryAdapter(private val dataSet: ArrayList<Long>) :
    RecyclerView.Adapter<LoginHistoryAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loginHistoryItemView: TextView = view.findViewById<TextView>(R.id.loginHistoryItemView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.login_history_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.loginHistoryItemView.text = "Logged in: ${getDateTime(dataSet[position])}"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    private fun getDateTime(s: Long): String {
        try {
            val pattern = "dd MMM yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)

            return simpleDateFormat.format(s)
        } catch (e: Exception) {
            print(e)
            return ""
        }
    }

}

