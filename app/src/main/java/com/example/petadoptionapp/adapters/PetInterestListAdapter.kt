package com.example.petadoptionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoptionapp.listeners.OnItemRemoveClickListener
import com.example.petadoptionapp.PetsModel
import com.example.petadoptionapp.R
import com.squareup.picasso.Picasso

class PetInterestListAdapter (private val dataSet: ArrayList<PetsModel>, private val onClickListener: OnItemRemoveClickListener) :
    RecyclerView.Adapter<PetInterestListAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petInterestNameView: TextView = view.findViewById(R.id.petInterestNameView)
        val petInterestAgeTypeView: TextView = view.findViewById(R.id.petInterestAgeTypeView)
        val petInterestInfoView: TextView = view.findViewById(R.id.petInterestInfoView)
        val petInterestImgView: ImageView = view.findViewById(R.id.petInterestImgView)
        val petInterestItemLayout: LinearLayout = view.findViewById(R.id.petInterestItemLayout)
        val removeInterestButton: TextView = view.findViewById(R.id.removeInterestButton)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pet_interest_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val imageUri:String = dataSet[position].url
        Picasso.get().load(imageUri).into(viewHolder.petInterestImgView);
        var typeAge:String = dataSet[position].type + ", "
        if(dataSet[position].age %10 ==1)
           typeAge += "${dataSet[position].age} year"
            else
            typeAge += "${dataSet[position].age} years"

        viewHolder.petInterestNameView.text = dataSet[position].name
        viewHolder.petInterestAgeTypeView.text = typeAge
        viewHolder.petInterestInfoView.text = "some info"
        viewHolder.removeInterestButton.setOnClickListener{
            onClickListener.onClicked(dataSet[position].id)
        }

    }

    override fun getItemCount() = dataSet.size

}
