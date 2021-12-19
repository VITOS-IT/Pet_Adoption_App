package com.example.petadoptionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoptionapp.listeners.OnItemClickListener
import com.example.petadoptionapp.PetsModel
import com.example.petadoptionapp.R
import com.squareup.picasso.Picasso

class PetListAdapter (private val dataSet: ArrayList<PetsModel>, private val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<PetListAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petNameView: TextView = view.findViewById(R.id.petNameView)
        val petAgeTypeView: TextView = view.findViewById(R.id.petAgeTypeView)
        val petInfoView: TextView = view.findViewById(R.id.petInfoView)
        val petImgView: ImageView = view.findViewById(R.id.petImgView)
        val petItemLayout: LinearLayout = view.findViewById(R.id.petItemLayout)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pet_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val imageUri:String = dataSet[position].url
        Picasso.get().load(imageUri).into(viewHolder.petImgView);
        var typeAge:String = dataSet[position].type + ", "
        if(dataSet[position].age %10 ==1)
           typeAge += "${dataSet[position].age} year"
            else
            typeAge += "${dataSet[position].age} years"

        viewHolder.petNameView.text = dataSet[position].name
        viewHolder.petAgeTypeView.text = typeAge
        viewHolder.petInfoView.text = "some info"

        viewHolder.petItemLayout.setOnClickListener{
            onClickListener.onClicked(dataSet[position])
        }

    }

    override fun getItemCount() = dataSet.size

}
