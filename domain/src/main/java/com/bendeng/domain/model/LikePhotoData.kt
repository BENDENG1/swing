package com.bendeng.domain.model

import com.bendeng.domain.model.base.BaseDomainModel

data class LikePhotoData(
    val photoInfo: PhotoInfoData
) : BaseDomainModel