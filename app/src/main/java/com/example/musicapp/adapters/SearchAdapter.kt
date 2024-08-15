package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.model.SearchAlbum
import com.example.musicapp.model.getExtralargeImageUrl

class SearchAdapter(
    private val albums: List<SearchAlbum>,
    private val onAlbumClick: (SearchAlbum) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album, onAlbumClick)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: SearchAlbum, onAlbumClick: (SearchAlbum) -> Unit) {
            itemView.findViewById<TextView>(R.id.albumTitle).text = album.name
            itemView.findViewById<TextView>(R.id.albumArtist).text = album.artist
            val imageUrl = album.getExtralargeImageUrl()
            Glide.with(itemView)
                .load(imageUrl)
                //.placeholder(R.drawable.placeholder_image) // Optional
                //.error(R.drawable.error_image) // Optional
                .into(itemView.findViewById(R.id.albumImageView))

            // Set up the click listener for the album image
            itemView.findViewById<View>(R.id.albumImageView).setOnClickListener {
                onAlbumClick(album)
            }
        }
    }
}
