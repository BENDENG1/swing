package com.bendeng.presentation.ui.feed

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bendeng.presentation.ui.FeedEvents
import com.bendeng.presentation.ui.MainSharedViewModel
import com.bendeng.presentation.R
import com.bendeng.presentation.base.BaseFragment
import com.bendeng.presentation.databinding.FragmentFeedBinding
import com.bendeng.presentation.ui.CommonEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val viewModel: MainSharedViewModel by activityViewModels()


    override fun initView() {
        with(binding) {
            vm = viewModel
            tietInputSearch.requestFocus()
            initAdapter()
        }
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.feedEvents.collect { event ->
                when (event) {
                    is FeedEvents.ShowSnackMessage -> showSnackBar(event.msg)
                    is FeedEvents.ScrollToTop -> binding.rvFeedPhoto.scrollToPosition(0)
                }
            }
        }
        repeatOnStarted {
            viewModel.commonEvents.collect { event ->
                when(event) {
                    is CommonEvents.ShowLoading -> showLoading(requireContext())
                    is CommonEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun FragmentFeedBinding.initAdapter() {
        rvFeedPhoto.adapter = SearchPhotoAdapter(object : PhotoClickListener {
            override fun onClick(id: String) {}

            override fun onLikeClick(id: String, isLike: Boolean) {
                viewModel.modifyLikePhoto(id, isLike)
            }
        })
        rvFeedPhoto.animation = null
        rvFeedPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollBottom = !rvFeedPhoto.canScrollVertically(1)
                val isNotLoading = !viewModel.feedUiState.value.isLoading
                if (scrollBottom && isNotLoading) {
                    viewModel.loadNextPhotos()
                }
            }
        })
    }

}