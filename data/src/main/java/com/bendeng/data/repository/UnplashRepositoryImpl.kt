package com.bendeng.data.repository


import com.bendeng.data.model.response.LikePhotoResponse.Companion.toDomainModel
import com.bendeng.data.model.response.SearchPhotoResponse.Companion.toDomainModel
import com.bendeng.data.model.response.UnLikePhotoResponse.Companion.toDomainModel
import com.bendeng.data.model.runRemote
import com.bendeng.data.remote.UnplashApi
import com.bendeng.domain.model.LikePhotoData
import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.domain.model.SearchPhotoData
import com.bendeng.domain.model.UnLikePhotoData
import com.bendeng.domain.model.base.BaseState
import com.bendeng.domain.repository.UnplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(
    private val api: UnplashApi
) : UnplashRepository {

    override fun getSearchPicture(
        query: String,
        page: Int,
        perPage: Int
    ): Flow<BaseState<SearchPhotoData>> =
        flow {
            when (val result = runRemote { api.getSearchPhotos(query, page, perPage) }) {
                is BaseState.Success -> {
                    emit(BaseState.Success(result.data.toDomainModel()))
                }

                is BaseState.Error -> {
                    emit(result)
                }
            }
        }

    override fun postLikePicture(id: String): Flow<BaseState<LikePhotoData>> = flow {
        when (val result = runRemote { api.postLikePhoto(id = id) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(result.data.toDomainModel()))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun deleteLikePicture(id: String): Flow<BaseState<UnLikePhotoData>> = flow {
        when (val result = runRemote { api.deleteUnlikePhoto(id = id) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(result.data.toDomainModel()))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getUserLikePicture(
        page: Int,
        perPage: Int
    ): Flow<BaseState<List<PhotoInfoData>>> = flow {
        when (val result = runRemote { api.getLikePhotos(page = page, perPage = perPage) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(result.data.map { it.toDomainModel() }))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }
}