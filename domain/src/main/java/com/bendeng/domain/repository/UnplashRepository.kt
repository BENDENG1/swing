package com.bendeng.domain.repository

import com.bendeng.domain.model.SearchPhotoData
import com.bendeng.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface UnplashRepository {

    fun getSearchPicture(query: String, page: Int): Flow<BaseState<SearchPhotoData>>
}