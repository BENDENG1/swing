package com.bendeng.presentation.ui.favorite

import androidx.fragment.app.activityViewModels
import com.bendeng.presentation.R
import com.bendeng.presentation.base.BaseFragment
import com.bendeng.presentation.databinding.FragmentFavoriteBinding
import com.bendeng.presentation.ui.CommonEvents
import com.bendeng.presentation.ui.FavoriteEvents
import com.bendeng.presentation.ui.MainSharedViewModel
import com.bendeng.presentation.ui.feed.PhotoClickListener
import com.bendeng.presentation.ui.feed.SearchPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    private val viewModel: MainSharedViewModel by activityViewModels()

    override fun initView() {
        with(binding) {
            vm = viewModel
            initAdapter()
        }
        viewModel.getLikePhotos()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.favoriteEvents.collect { event ->
                when (event) {
                    is FavoriteEvents.ShowSnackMessage -> showSnackBar(event.msg)
                    is FavoriteEvents.ScrollToTop -> binding.rvFavorite.scrollToPosition(0)
                }
            }
        }
        repeatOnStarted {
            viewModel.commonEvents.collect { event ->
                when (event) {
                    is CommonEvents.ShowLoading -> showLoading(requireContext())
                    is CommonEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun FragmentFavoriteBinding.initAdapter() {
        rvFavorite.adapter = SearchPhotoAdapter(object : PhotoClickListener {
            override fun onClick(id: String) {}

            override fun onLikeClick(id: String, isLike: Boolean) {
                viewModel.modifyLikePhoto(id, isLike)
            }
        })
        rvFavorite.animation = null
    }
}