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
        private val trackNameTextView: TextView = itemView.findViewById(R.id.trackTitleTextView)
        private val thumbsUpButton: ImageView = itemView.findViewById(R.id.thumbsUpImageView)
        private val thumbsDownButton: ImageView = itemView.findViewById(R.id.thumbsDownImageView)

        private var isThumbsUp = false

        init {
            thumbsUpButton.setOnClickListener {
                toggleThumbs(true)
            }
            thumbsDownButton.setOnClickListener {
                toggleThumbs(false)
            }
        }

        fun bind(track: Track) {
            trackNameTextView.text = track.name
        }

        private fun toggleThumbs(isUp: Boolean) {
            if (isUp) {
                thumbsUpButton.setImageResource(R.drawable.ic_thumbs_up_selected)
                thumbsDownButton.setImageResource(R.drawable.ic_thumbs_down)
                isThumbsUp = true
            } else {
                thumbsUpButton.setImageResource(R.drawable.ic_thumbs_up)
                thumbsDownButton.setImageResource(R.drawable.ic_thumbs_down_selected)
                isThumbsUp = false
            }
        }
    }
}
