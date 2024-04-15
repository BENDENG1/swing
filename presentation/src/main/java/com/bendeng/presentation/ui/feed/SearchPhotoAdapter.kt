package com.bendeng.presentation.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.presentation.databinding.SearchPhotoListBinding

class SearchPhotoAdapter(
    private val photoClickListener: PhotoClickListener
) : ListAdapter<PhotoInfoData, SearchPhotoViewHolder>(diffCallBack) {

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<PhotoInfoData>() {
            override fun areItemsTheSame(
                oldItem: PhotoInfoData,
                newItem: PhotoInfoData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PhotoInfoData,
                newItem: PhotoInfoData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPhotoViewHolder {
        return SearchPhotoViewHolder(
            SearchPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            photoClickListener = photoClickListener
        )
    }

    override fun onBindViewHolder(holder: SearchPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class SearchPhotoViewHolder(
    private val binding: SearchPhotoListBinding,
    private val photoClickListener: PhotoClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PhotoInfoData) {
        with(binding) {
            this.item = item
            setClickListener(item)
            executePendingBindings()
        }
    }


    private fun SearchPhotoListBinding.setClickListener(item: PhotoInfoData) {
        ivLike.setOnClickListener {
            photoClickListener.onLikeClick(item.id, item.isLike)
        }
        root.setOnClickListener {
            photoClickListener.onClick(item.id)
        }
    }
}

interface PhotoClickListener {
    fun onClick(id: String)
    fun onLikeClick(id: String, isLike: Boolean)
}