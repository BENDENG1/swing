package com.bendeng.data.model.response

import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.domain.model.UrlInfoData
import com.google.gson.annotations.SerializedName

data class PhotoInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("urls")
    val url: UrlInfo,
    @SerializedName("liked_by_user")
    val isLike: Boolean
) {
    fun toDomainModel(): PhotoInfoData {
        return PhotoInfoData(
            id = id,
            url = url.toDomainModel(),
            isLike = isLike
        )
    }
}

data class UrlInfo(
    @SerializedName("raw")
    val raw: String,
    @SerializedName("full")
    val full: String,
    @SerializedName("regular")
    val regular: String,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("small")
    val small: String
) {
    fun toDomainModel(): UrlInfoData {
        return UrlInfoData(
            small = small
        )
    }
}
