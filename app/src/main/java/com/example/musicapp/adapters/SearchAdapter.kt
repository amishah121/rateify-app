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

class SearchAdapter(private val albums: List<SearchAlbum>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: SearchAlbum) {
            // Bind album data to views in ViewHolder
            itemView.findViewById<TextView>(R.id.albumTitle).text = album.name
            itemView.findViewById<TextView>(R.id.albumArtist).text = album.artist
            // Load album cover image using Glide or another image loading library
            val imageUrl = album.getExtralargeImageUrl()
            Glide.with(itemView)
                .load(imageUrl)
                //.placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                //.error(R.drawable.error_image) // Error image if loading fails
                .into(itemView.findViewById(R.id.albumImageView))
        }
    }
}
