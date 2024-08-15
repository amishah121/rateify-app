package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Track

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(track: Track) {
            val trackNameTextView = itemView.findViewById<TextView>(R.id.trackTitleTextView)
            val thumbsUpButton = itemView.findViewById<ImageView>(R.id.thumbsUpImageView)
            val thumbsDownButton = itemView.findViewById<ImageView>(R.id.thumbsDownImageView)

            trackNameTextView.text = track.name
            // Set up thumbs up/down click listeners if needed
        }
    }
}
