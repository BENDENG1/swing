package com.bendeng.data.model.response

import com.bendeng.data.model.base.BaseDataModel
import com.bendeng.data.model.mapper.DomainMapper
import com.bendeng.domain.model.UnLikePhotoData
import com.google.gson.annotations.SerializedName

data class UnLikePhotoResponse(
    @SerializedName("photo")
    val photoInfo: PhotoInfo
) : BaseDataModel {
    companion object : DomainMapper<UnLikePhotoResponse, UnLikePhotoData> {
        override fun UnLikePhotoResponse.toDomainModel(): UnLikePhotoData =
            UnLikePhotoData(photoInfo = photoInfo.toDomainModel())
    }
}