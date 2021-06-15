package com.atilsamancioglu.artbooknavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.atilsamancioglu.artbooknavigation.databinding.ItemRowBinding

class ArtListAdapter(val artNameList: ArrayList<String>, val artIdList: ArrayList<Int>) : RecyclerView.Adapter<ArtListAdapter.ArtHolder>() {

    class ArtHolder(val binding : ItemRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }

    override fun getItemCount(): Int {
        return artNameList.size
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {

        holder.binding.artNameText.text = artNameList[position]
        holder.itemView.setOnClickListener {
            val action = ArtListDirections.actionArtListToDetailFragment(artIdList[position],"old")
            Navigation.findNavController(it).navigate(action)

        }
    }
}


