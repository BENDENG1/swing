package com.bendeng.domain.model

import com.bendeng.domain.model.base.BaseDomainModel

data class SearchPhotoData(
    val totalPages : Int,
    val searchPhotoInfo : List<SearchPhotoInfoData>
) : BaseDomainModel

data class SearchPhotoInfoData(
    val id : String,
    val url : UrlInfoData
)

data class UrlInfoData(
    val small : String
)