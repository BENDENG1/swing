package com.bendeng.data.model.mapper

import com.bendeng.data.model.base.BaseDataModel
import com.bendeng.domain.model.base.BaseDomainModel

interface DomainMapper<in R : BaseDataModel, out D : BaseDomainModel> {
    fun R.toDomainModel() : D
}