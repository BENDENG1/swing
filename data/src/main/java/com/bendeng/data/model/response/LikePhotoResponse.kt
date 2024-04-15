package com.bendeng.data.model.response

import com.bendeng.data.model.base.BaseDataModel
import com.bendeng.data.model.mapper.DomainMapper
import com.bendeng.domain.model.LikePhotoData
import com.google.gson.annotations.SerializedName

data class LikePhotoResponse(
    @SerializedName("photo")
    val photoInfo: PhotoInfo
) : BaseDataModel {
    companion object : DomainMapper<LikePhotoResponse, LikePhotoData> {
        override fun LikePhotoResponse.toDomainModel(): LikePhotoData =
            LikePhotoData(photoInfo = photoInfo.toDomainModel())
    }
}

