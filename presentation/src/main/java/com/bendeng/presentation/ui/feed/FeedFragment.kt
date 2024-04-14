package com.bendeng.presentation.ui.feed

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bendeng.presentation.R
import com.bendeng.presentation.base.BaseFragment
import com.bendeng.presentation.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by viewModels()
    private val adapter = SearchPhotoAdapter()

    override fun initView() {
        with(binding) {
            vm = viewModel
            rvFeedPhoto.adapter = adapter
            rvFeedPhoto.animation = null
            rvFeedPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val scrollBottom = !rvFeedPhoto.canScrollVertically(1)
                    val isNotLoading = !viewModel.uiState.value.isLoading
                    if (scrollBottom && isNotLoading) {
                        viewModel.loadNextPhotos()
                    }
                }
            })
            tietInputSearch.requestFocus()
        }
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is FeedEvents.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }
    }

}