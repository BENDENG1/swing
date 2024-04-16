package com.bendeng.presentation.ui.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bendeng.presentation.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("feedImageUrl")
fun bindLoadImage(view: ImageView, imageUrl: String) {
    if (imageUrl.isNotBlank()) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_image_empty)
            .apply(RequestOptions.fitCenterTransform())
            .error(R.drawable.ic_image_fail)
            .into(view)
    }else{
        Glide.with(view.context)
            .load(R.drawable.ic_image_empty)
            .apply(RequestOptions.fitCenterTransform())
            .into(view)
    }
}