package com.shivamgupta.newsapp.presentation.bookmark_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.FragmentBookmarkNewsBinding
import com.shivamgupta.newsapp.domain.models.News
import com.shivamgupta.newsapp.presentation.news_feed.adapter.NewsRecyclerViewAdapter
import com.shivamgupta.newsapp.util.showSnackBar
import com.shivamgupta.newsapp.util.toast
import kotlinx.coroutines.launch


class BookmarkNewsFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkNewsBinding
    private val viewModel by activityViewModels<BookmarkNewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
       binding = DataBindingUtil.inflate(
           inflater,
           R.layout.fragment_bookmark_news,
           container,
           false
       )

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeToRefreshLayout.apply {
            setOnRefreshListener {
                toast(text = context.getString(R.string.news_updated_message))
                viewModel.getBookmarkedNews()
            }
        }
    }

    private fun setupViews() {
        setupSwipeToRefresh()

        viewModel.getBookmarkedNews()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                lifecycle
            ).collect { currentState ->
                binding.isLoading = currentState.isLoading

                currentState.userMessage?.let {
                    showSnackBar(
                        message = it,
                        rootLayout = binding.root,
                    )

                    viewModel.userMessageShown()
                }

                setupNewsRecyclerView(
                    currentState.news
                )
            }
        }
    }

    private fun setupNewsRecyclerView(news: List<News>) {
        binding.bookmarkedNewsRecyclerView.adapter = NewsRecyclerViewAdapter(
            news = news,
            onNewsClicked = { clickedNews ->
                findNavController().navigate(
                    BookmarkNewsFragmentDirections.openNewsDetailScreenFromBookmarkNewsScreen(
                        newsItem = clickedNews,
                        navigateFromBookmarkedScreen = true
                    )
                )
            }
        )
    }

}