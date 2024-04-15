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


    override fun initView() {
        with(binding) {
            vm = viewModel
            tietInputSearch.requestFocus()
            initAdapter()
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
                val isNotLoading = !viewModel.uiState.value.isLoading
                if (scrollBottom && isNotLoading) {
                    viewModel.loadNextPhotos()
                }
            }
        })
    }

}