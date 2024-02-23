package com.shivamgupta.newsapp.presentation.news_feed

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
import com.shivamgupta.newsapp.databinding.FragmentNewsFeedBinding
import com.shivamgupta.newsapp.domain.models.News
import com.shivamgupta.newsapp.presentation.news_feed.adapter.NewsRecyclerViewAdapter
import com.shivamgupta.newsapp.util.Constants
import com.shivamgupta.newsapp.util.showSnackBar
import com.shivamgupta.newsapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFeedFragment : Fragment() {

    private lateinit var binding: FragmentNewsFeedBinding
    private val viewModel by activityViewModels<NewsFeedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_news_feed,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        setupSwipeToRefresh()

        viewModel.getTopHeadlines(
            countryCode = Constants.INDIA_COUNTRY_CODE
        )

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


    private fun setupSwipeToRefresh() {
        binding.swipeToRefreshLayout.apply {
            setOnRefreshListener {
                toast(text = context.getString(R.string.news_updated_message))
                viewModel.getTopHeadlines(
                    fetchFromRemote = true, countryCode = Constants.INDIA_COUNTRY_CODE
                )
            }
        }
    }

    private fun setupNewsRecyclerView(news: List<News>) {
        binding.newsRecyclerView.adapter = NewsRecyclerViewAdapter(
            news = news,
            onNewsClicked = { clickedNews ->
                findNavController().navigate(NewsFeedFragmentDirections.openNewsDetailScreenFromNewsFeedScreen(clickedNews))
            }
        )
    }
}