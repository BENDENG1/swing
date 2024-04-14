package com.bendeng.presentation.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bendeng.domain.model.SearchPhotoInfoData
import com.bendeng.presentation.databinding.SearchPhotoListBinding

class SearchPhotoAdapter(
//    private val addWish: (String) -> Unit
) : ListAdapter<SearchPhotoInfoData, SearchPhotoViewHolder>(diffCallBack) {

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<SearchPhotoInfoData>() {
            override fun areItemsTheSame(
                oldItem: SearchPhotoInfoData,
                newItem: SearchPhotoInfoData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SearchPhotoInfoData,
                newItem: SearchPhotoInfoData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPhotoViewHolder {
        return SearchPhotoViewHolder(
            SearchPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class SearchPhotoViewHolder(
    private val binding: SearchPhotoListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SearchPhotoInfoData) {
        with(binding) {
            this.item = item
        }
    }
}