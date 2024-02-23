package com.shivamgupta.newsapp.presentation.binding_adapters

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.util.getColorResById

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ShapeableImageView, url: String?){
    if(url.isNullOrEmpty()) {
        imageView.setImageResource(R.drawable.ic_image_placeholder)
    } else {
        Glide.with(imageView)
            .load(url)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_error)
            .into(imageView)

    }
}

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?){
    if(url.isNullOrEmpty()) {
        val color = imageView.getColorResById(R.color.blank_header_image_bg_color)

        imageView.setBackgroundColor(color)
    } else {
        Glide.with(imageView)
            .load(url)
            .placeholder(R.drawable.layout_news_img_placeholder)
            .error(R.drawable.ic_image_error)
            .into(imageView)

    }
}