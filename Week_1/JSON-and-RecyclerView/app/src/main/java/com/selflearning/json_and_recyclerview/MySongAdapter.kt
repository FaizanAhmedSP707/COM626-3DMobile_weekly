package com.selflearning.json_and_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MySongAdapter(var songs: List<Song>,
                    val callback: (Int) -> Unit):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSongName = itemView.findViewById(R.id.SongTitleTextView) as TextView
        val tvSongDesc = itemView.findViewById(R.id.SongDescriptionTextView) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.list_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val myViewHolder = holder as MyViewHolder
        // Use the single list of songs to display different data in different TextViews inside the RecyclerView
        myViewHolder.tvSongName.text = songs[index].title
        myViewHolder.tvSongDesc.text = songs[index].artist + ", " +songs[index].year

        /*
        For displaying a message using a Toast, we need to have a callback with an integer
        referring to the index of the song item within the list.
        Within the adapter, the keyword 'it' will refer to the index of the song item
        */
        myViewHolder.itemView.setOnClickListener { callback(index) }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}