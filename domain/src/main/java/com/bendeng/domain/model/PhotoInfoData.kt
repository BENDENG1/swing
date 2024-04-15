package com.bendeng.domain.model

data class PhotoInfoData(
    val id: String,
    val url: UrlInfoData,
    val isLike: Boolean
)

data class UrlInfoData(
    val small: String
)