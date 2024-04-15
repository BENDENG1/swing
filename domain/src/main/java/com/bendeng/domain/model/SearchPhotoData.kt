package com.bendeng.domain.model

import com.bendeng.domain.model.base.BaseDomainModel

data class SearchPhotoData(
    val totalPages: Int,
    val photoInfo: List<PhotoInfoData>
) : BaseDomainModel