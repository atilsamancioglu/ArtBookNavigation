package com.atilsamancioglu.artbooknavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class ArtListAdapter(val artNameList: ArrayList<String>, val artIdList: ArrayList<Int>) : RecyclerView.Adapter<ArtListAdapter.ArtHolder>() {

    class ArtHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_row,parent,false)
        return ArtHolder(view)

    }

    override fun getItemCount(): Int {
        return artNameList.size
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {

        holder.itemView.artNameText.text = artNameList[position]
        holder.itemView.setOnClickListener {
            val action = ArtListDirections.actionArtListToDetailFragment(artIdList[position],"old")
            Navigation.findNavController(it).navigate(action)

        }
    }
}


