package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.model.Album
import com.example.musicapp.model.getExtralargeImageUrl

class AlbumAdapter(
    private val albums: List<Album>,
    private val onAlbumClickListener: (Album) -> Unit // Use a lambda to handle the click event
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album, onAlbumClickListener)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: Album, onAlbumClickListener: (Album) -> Unit) {
            val albumTitle = itemView.findViewById<TextView>(R.id.albumTitle)
            val albumArtist = itemView.findViewById<TextView>(R.id.albumArtist)
            val albumImageView = itemView.findViewById<ImageView>(R.id.albumImageView)

            albumTitle.text = album.name
            albumArtist.text = album.artist.name

            // Load album cover image using Glide or another image loading library
            val imageUrl = album.getExtralargeImageUrl()
            Glide.with(itemView.context)
                .load(imageUrl)
                //.placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                //.error(R.drawable.error_image) // Error image if loading fails
                .into(albumImageView)

            // Set click listener on album image
            albumImageView.setOnClickListener {
                onAlbumClickListener(album)
            }
        }
    }
}
