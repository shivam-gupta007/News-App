package com.shivamgupta.newsapp.presentation.search_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.FragmentNewsSearchBinding
import com.shivamgupta.newsapp.presentation.search_news.adapter.PagerLoaderAdapter
import com.shivamgupta.newsapp.presentation.search_news.adapter.SearchedNewsPagingAdapter
import com.shivamgupta.newsapp.util.ExceptionUtils
import com.shivamgupta.newsapp.util.afterTextChanged
import com.shivamgupta.newsapp.util.editTextValue
import com.shivamgupta.newsapp.util.hideKeyboard
import com.shivamgupta.newsapp.util.showKeyboard
import com.shivamgupta.newsapp.util.showSnackBar
import com.shivamgupta.newsapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsSearchFragment : Fragment() {

    private lateinit var binding: FragmentNewsSearchBinding
    private val viewModel by activityViewModels<SearchNewsViewModel>()
    private lateinit var searchedNewsPagingAdapter: SearchedNewsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_search, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        viewModel.clearUiState()

        setupNewsSearchBox()
        setupNewsRecyclerView()
        observeLoadingState()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                lifecycle
            ).collectLatest { currentUiState ->
                binding.apply {
                    headerTitleText = currentUiState.headerTitleText
                    searchedText = currentUiState.searchedText
                }

                searchedNewsPagingAdapter.submitData(currentUiState.searchedNews)
            }
        }
    }


    private fun setupNewsRecyclerView() {
        searchedNewsPagingAdapter = SearchedNewsPagingAdapter(
            onNewsClicked = { clickedNews ->
                findNavController().navigate(
                    NewsSearchFragmentDirections.openNewsDetailScreenFromSearchNewsScreen(
                        newsItem = clickedNews,
                        navigatedFromSearchNewsScreen = true
                    )
                )
            }
        )

        binding.searchedNewsRecyclerView.adapter = searchedNewsPagingAdapter.withLoadStateFooter(
            footer = PagerLoaderAdapter()
        )
    }

    private fun observeLoadingState() {
        if (::searchedNewsPagingAdapter.isInitialized) {
            viewLifecycleOwner.lifecycleScope.launch {
                searchedNewsPagingAdapter.loadStateFlow.flowWithLifecycle(
                    lifecycle
                ).collect { combinedLoadStates ->
                    when (combinedLoadStates.refresh) {
                        is LoadState.Error -> {
                            handleApiError(loadState = combinedLoadStates.refresh)
                            viewModel.updateHeaderTitleTextState(text = null)
                            binding.isLoading = false
                        }

                        is LoadState.Loading -> {
                            binding.isLoading = true
                        }

                        is LoadState.NotLoading -> {
                            binding.isLoading = false
                            val noMatchingNewsFound = searchedNewsPagingAdapter.snapshot().isEmpty()
                            if(noMatchingNewsFound){
                                val searchQuery = binding.searchNewsInputLayout.editTextValue
                                viewModel.updateHeaderTitleTextState(text = getString(R.string.no_matching_news_found_message, searchQuery))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleApiError(loadState: LoadState){
        val exception = (loadState as LoadState.Error).error
        val errorMessage = ExceptionUtils.getUiMessageOfException(
            throwable = exception,
            context = requireContext()
        )
        showSnackBar(
            message = errorMessage,
            rootLayout = binding.root
        )
    }

    private fun setupNewsSearchBox() {
        binding.searchNewsInputLayout.apply {
            editText?.showKeyboard()

            editText?.setOnEditorActionListener { _, actionId, _ ->
                val isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
                if (isSearchAction) {
                    searchNews()
                }

                true
            }

            afterTextChanged { text ->
                viewModel.updateSearchTextState(text)
            }

            setEndIconOnClickListener {
                viewModel.updateSearchTextState(text = null)
            }
        }
    }

    private fun searchNews() {
        val searchQuery = binding.searchNewsInputLayout.editTextValue
        if (searchQuery.isNotEmpty()) {
            viewModel.updateHeaderTitleTextState(text = null)
            viewModel.searchNews()
            binding.searchNewsInputLayout.editText?.hideKeyboard()
        } else {
            toast(text = getString(R.string.empty_search_text_message))
        }
    }


}