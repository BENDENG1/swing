package com.bendeng.presentation.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bendeng.domain.model.PhotoInfoData
import com.bendeng.domain.model.base.BaseState
import com.bendeng.domain.repository.UnplashRepository
import com.bendeng.presentation.util.Constants.NETWORK_ERROR
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

sealed class FeedEvents {
    data class ShowSnackMessage(val msg: String) : FeedEvents()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val unplashRepository: UnplashRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    val query = MutableStateFlow("")

    private val _events = MutableSharedFlow<FeedEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<FeedEvents> = _events.asSharedFlow()

    fun loadPhotos() {
        _uiState.update { it.copy(isLoading = true) }
        unplashRepository.getSearchPicture(query.value, FIRST_PAGE, PER_PAGE).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    val item = state.data
                    if (item.photoInfo.isEmpty()) {
                        _events.emit(FeedEvents.ShowSnackMessage(SEARCH_EMPTY))
                    } else {
                        _uiState.update { ui ->
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
                    _events.emit(FeedEvents.ShowSnackMessage(NETWORK_ERROR))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextPhotos() {
        if (uiState.value.page < uiState.value.totalPage) {
            _uiState.update { it.copy(page = uiState.value.page + 1, isLoading = true) }
            unplashRepository.getSearchPicture(query.value, uiState.value.page, PER_PAGE)
                .onEach { state ->
                    when (state) {
                        is BaseState.Success -> {
                            val item = state.data
                            _uiState.update { ui ->
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
                            _events.emit(FeedEvents.ShowSnackMessage(NETWORK_ERROR))
                        }
                    }
                }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                _events.emit(FeedEvents.ShowSnackMessage(SEARCH_LAST))
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
                    _uiState.update { ui ->
                        val updatedPhotoList = uiState.value.photoList.map { photo ->
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
                    _events.emit(FeedEvents.ShowSnackMessage(NETWORK_ERROR))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun unlikePhoto(id: String) {
        unplashRepository.deleteLikePicture(id).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    _uiState.update { ui ->
                        val updatedPhotoList = uiState.value.photoList.map { photo ->
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
                }

                is BaseState.Error -> {
                    _events.emit(FeedEvents.ShowSnackMessage(NETWORK_ERROR))
                }
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val PER_PAGE = 30
    }

}