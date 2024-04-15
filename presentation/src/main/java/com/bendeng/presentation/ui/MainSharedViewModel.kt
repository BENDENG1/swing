package com.bendeng.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.domain.model.base.BaseState
import com.bendeng.domain.repository.UnplashRepository
import com.bendeng.presentation.util.Constants.SEARCH_EMPTY
import com.bendeng.presentation.util.Constants.SEARCH_LAST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class FeedUiState(
    val page: Int = 1,
    val totalPage: Int = 0,
    val photoList: List<PhotoInfoData> = emptyList(),
    val isLoading: Boolean = false,
)

data class FavoriteUiState(
    val photoList: List<PhotoInfoData> = emptyList()
)

sealed class FeedEvents {
    data class ShowSnackMessage(val msg: String) : FeedEvents()
    data object ScrollToTop : FeedEvents()
}

sealed class FavoriteEvents {
    data class ShowSnackMessage(val msg: String) : FavoriteEvents()
    data object ScrollToTop : FavoriteEvents()
}

@HiltViewModel
class MainSharedViewModel @Inject constructor(
    private val unplashRepository: UnplashRepository
) : ViewModel() {

    private val _feedUiState = MutableStateFlow(FeedUiState())
    val feedUiState: StateFlow<FeedUiState> = _feedUiState.asStateFlow()

    private val _favoriteUiState = MutableStateFlow(FavoriteUiState())
    val favoriteUiState: StateFlow<FavoriteUiState> = _favoriteUiState.asStateFlow()

    val query = MutableStateFlow("")

    private val _feedEvents = MutableSharedFlow<FeedEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val feedEvents: SharedFlow<FeedEvents> = _feedEvents.asSharedFlow()

    private val _favoriteEvents = MutableSharedFlow<FavoriteEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val favoriteEvents: SharedFlow<FavoriteEvents> = _favoriteEvents.asSharedFlow()

    fun loadPhotos() {
        _feedUiState.update { it.copy(isLoading = true) }
        unplashRepository.getSearchPicture(query.value, FIRST_PAGE, PER_PAGE).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    val item = state.data
                    if (item.photoInfo.isEmpty()) {
                        _feedEvents.emit(FeedEvents.ShowSnackMessage(SEARCH_EMPTY))
                    } else {
                        _feedUiState.update { ui ->
                            ui.copy(
                                page = FIRST_PAGE,
                                totalPage = item.totalPages,
                                photoList = item.photoInfo,
                                isLoading = false
                            )
                        }
                    }
                }

                is BaseState.Error -> {
                    _feedEvents.emit(FeedEvents.ShowSnackMessage(state.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextPhotos() {
        if (feedUiState.value.page < feedUiState.value.totalPage) {
            _feedUiState.update { it.copy(page = feedUiState.value.page + 1, isLoading = true) }
            unplashRepository.getSearchPicture(query.value, feedUiState.value.page, PER_PAGE)
                .onEach { state ->
                    when (state) {
                        is BaseState.Success -> {
                            val item = state.data
                            _feedUiState.update { ui ->
                                val updatedList = ui.photoList.toMutableList().apply {
                                    addAll(item.photoInfo)
                                }
                                ui.copy(
                                    photoList = updatedList,
                                    totalPage = item.totalPages,
                                    isLoading = false
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _feedEvents.emit(FeedEvents.ShowSnackMessage(state.message))
                        }
                    }
                }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                _feedEvents.emit(FeedEvents.ShowSnackMessage(SEARCH_LAST))
            }
        }
    }

    fun modifyLikePhoto(id: String, isLike: Boolean) {
        when (isLike) {
            true -> unlikePhoto(id)
            false -> likePhoto(id)
        }
    }

    private fun likePhoto(id: String) {
        unplashRepository.postLikePicture(id).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    _feedUiState.update { ui ->
                        val updatedPhotoList = feedUiState.value.photoList.map { photo ->
                            if (photo.id == id) {
                                photo.copy(isLike = true)
                            } else {
                                photo
                            }
                        }
                        ui.copy(
                            photoList = updatedPhotoList
                        )
                    }
                }

                is BaseState.Error -> {
                    _feedEvents.emit(FeedEvents.ShowSnackMessage(state.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun unlikePhoto(id: String) {
        unplashRepository.deleteLikePicture(id).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    _feedUiState.update { ui ->
                        val updatedPhotoList = feedUiState.value.photoList.map { photo ->
                            if (photo.id == id) {
                                photo.copy(isLike = false)
                            } else {
                                photo
                            }
                        }
                        ui.copy(
                            photoList = updatedPhotoList
                        )
                    }
                    _favoriteUiState.update { ui ->
                        val updatedLikeList =
                            favoriteUiState.value.photoList.filterNot { it.id == id }
                        ui.copy(photoList = updatedLikeList)
                    }
                }

                is BaseState.Error -> {
                    _feedEvents.emit(FeedEvents.ShowSnackMessage(state.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getLikePhotos() {
        unplashRepository.getUserLikePicture(FIRST_PAGE, PER_PAGE).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    _favoriteUiState.update { ui ->
                        ui.copy(
                            photoList = state.data
                        )
                    }
                }

                is BaseState.Error -> {
                    _favoriteEvents.emit(FavoriteEvents.ShowSnackMessage(state.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val PER_PAGE = 30
    }

}