package com.selflearning.json_and_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MySongAdapter(var songTitles: MutableList<String>,
                    var songDescriptions: MutableList<String>,
                    val callback: (Int) -> Unit):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSongName = itemView.findViewById(R.id.SongNameTextView) as TextView
        val tvSongDescription = itemView.findViewById(R.id.SongDescriptionTextView) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.list_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.tvSongName.text = songTitles[index]
        myViewHolder.tvSongDescription.text = songDescriptions[index]

        /*
        For displaying a message using a Toast, we need to have a callback with an integer
        referring to the index of the song item within the list.
        Within the adapter, the keyword 'it' will refer to the index of the song item
        */
        myViewHolder.itemView.setOnClickListener { callback(index) }
    }

    override fun getItemCount(): Int {
        return songTitles.size
    }
}