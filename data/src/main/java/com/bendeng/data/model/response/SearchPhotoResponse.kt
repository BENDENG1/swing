package com.bendeng.data.model.response

import com.bendeng.data.model.base.BaseDataModel
import com.bendeng.data.model.mapper.DomainMapper
import com.bendeng.domain.model.SearchPhotoData
import com.bendeng.domain.model.SearchPhotoInfoData
import com.bendeng.domain.model.UrlInfoData
import com.google.gson.annotations.SerializedName

data class SearchPhotoResponse (
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("results")
    val searchPhotoInfo: List<SearchPhotoInfo>,
) : BaseDataModel {
    companion object : DomainMapper<SearchPhotoResponse, SearchPhotoData>{
        override fun SearchPhotoResponse.toDomainModel(): SearchPhotoData = SearchPhotoData(
            totalPages = totalPages,
            searchPhotoInfo = searchPhotoInfo.map { it.toDomainModel() }
        )
    }
}

data class SearchPhotoInfo(
    @SerializedName("id")
    val id : String,
    @SerializedName("urls")
    val url : UrlInfo
) {
    fun toDomainModel(): SearchPhotoInfoData {
        return SearchPhotoInfoData(
            id = id,
            url = url.toDomainModel()
        )
    }
}

data class UrlInfo(
    @SerializedName("raw")
    val raw : String,
    @SerializedName("full")
    val full : String,
    @SerializedName("regular")
    val regular : String,
    @SerializedName("thumb")
    val thumb : String,
    @SerializedName("small")
    val small : String
) {
    fun toDomainModel(): UrlInfoData {
        return UrlInfoData(
            small = small
        )
    }
}