package com.bendeng.data.model.response

import com.bendeng.data.model.base.BaseDataModel
import com.bendeng.data.model.mapper.DomainMapper
import com.bendeng.domain.model.SearchPhotoData
import com.google.gson.annotations.SerializedName

data class SearchPhotoResponse(
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val photoInfo: List<PhotoInfo>,
) : BaseDataModel {
    companion object : DomainMapper<SearchPhotoResponse, SearchPhotoData> {
        override fun SearchPhotoResponse.toDomainModel(): SearchPhotoData = SearchPhotoData(
            totalPages = totalPages,
            photoInfo = photoInfo.map { it.toDomainModel() }
        )
    }
}