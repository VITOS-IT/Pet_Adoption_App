package com.example.petadoptionapp

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import com.squareup.picasso.Picasso

class PetListAdapter (private val dataSet: ArrayList<PetsModel>, private val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<PetListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petNameView: TextView = view.findViewById(R.id.petNameView)
        val petAgeTypeView: TextView = view.findViewById(R.id.petAgeTypeView)
        val petInfoView: TextView = view.findViewById(R.id.petInfoView)
        val petImgView: ImageView = view.findViewById(R.id.petImgView)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pet_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
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

//        val urlBitmap = dataSet[position].url.toBitmap
//viewHolder.petImgView.setImageResource()
        viewHolder.petNameView.setOnClickListener{
            onClickListener.onClickek(dataSet[position].name)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
