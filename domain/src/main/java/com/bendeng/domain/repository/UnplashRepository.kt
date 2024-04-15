package com.bendeng.domain.repository

import com.bendeng.domain.model.LikePhotoData
import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.domain.model.SearchPhotoData
import com.bendeng.domain.model.UnLikePhotoData
import com.bendeng.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface UnplashRepository {

    fun getSearchPicture(query: String, page: Int, perPage : Int): Flow<BaseState<SearchPhotoData>>

    fun postLikePicture(id: String): Flow<BaseState<LikePhotoData>>

    fun deleteLikePicture(id: String): Flow<BaseState<UnLikePhotoData>>

    fun getUserLikePicture(page : Int, perPage : Int) : Flow<BaseState<List<PhotoInfoData>>>
}