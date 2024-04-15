package com.bendeng.domain.model

import com.bendeng.domain.model.base.BaseDomainModel

data class UnLikePhotoData(
    val photoInfo: PhotoInfoData
) : BaseDomainModel