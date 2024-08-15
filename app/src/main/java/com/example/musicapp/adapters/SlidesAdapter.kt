package com.example.musicapp.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class SlidesAdapter(
    private val albumImages: List<String>,
    private val onImageClick: (Int) -> Unit // Click listener with the position of the clicked image
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setOnClickListener {
                onImageClick(position) // Trigger the click listener with the image position
            }
        }
        Glide.with(container.context).load(albumImages[position]).into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = albumImages.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
}
