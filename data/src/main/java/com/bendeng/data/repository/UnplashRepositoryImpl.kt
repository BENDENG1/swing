package com.bendeng.data.repository


import com.bendeng.data.model.response.SearchPhotoResponse.Companion.toDomainModel
import com.bendeng.data.model.runRemote
import com.bendeng.data.remote.UnplashApi
import com.bendeng.domain.model.SearchPhotoData
import com.bendeng.domain.model.base.BaseState
import com.bendeng.domain.repository.UnplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(
    private val api: UnplashApi
) : UnplashRepository {

    override fun getSearchPicture(query: String, page: Int): Flow<BaseState<SearchPhotoData>> =
        flow {
            when (val result = runRemote { api.getSearchPhotos(query, page) }) {
                is BaseState.Success -> {
                    emit(BaseState.Success(result.data.toDomainModel()))
                }

                is BaseState.Error -> {
                    emit(result)
                }
            }
        }
}