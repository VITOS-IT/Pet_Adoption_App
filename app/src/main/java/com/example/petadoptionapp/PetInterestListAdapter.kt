package com.example.petadoptionapp

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.implementbooks.OnItemClickListener
import com.example.implementbooks.OnItemRemoveClickListener
import com.squareup.picasso.Picasso

class PetInterestListAdapter (private val dataSet: ArrayList<PetsModel>, private val onClickListener: OnItemRemoveClickListener) :
    RecyclerView.Adapter<PetInterestListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petInterestNameView: TextView = view.findViewById(R.id.petInterestNameView)
        val petInterestAgeTypeView: TextView = view.findViewById(R.id.petInterestAgeTypeView)
        val petInterestInfoView: TextView = view.findViewById(R.id.petInterestInfoView)
        val petInterestImgView: ImageView = view.findViewById(R.id.petInterestImgView)
        val petInterestItemLayout: LinearLayout = view.findViewById(R.id.petInterestItemLayout)
        val removeInterestButton: TextView = view.findViewById(R.id.removeInterestButton)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pet_interest_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
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

//        val urlBitmap = dataSet[position].url.toBitmap
//viewHolder.petImgView.setImageResource()
        viewHolder.removeInterestButton.setOnClickListener{
            onClickListener.onClicked(dataSet[position].id)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
