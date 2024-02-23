package com.shivamgupta.newsapp.presentation.search_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.FragmentSearchNewsBinding
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
class SearchNewsFragment : Fragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    private val viewModel by activityViewModels<SearchNewsViewModel>()
    private lateinit var searchedNewsPagingAdapter: SearchedNewsPagingAdapter
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_news, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        setupNewsSearchBox()
        setupNewsRecyclerView()
        observeLoadingState()
        handleOnBackPressed()

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

    private fun handleOnBackPressed() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clearUiState()
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun setupNewsRecyclerView() {
        searchedNewsPagingAdapter = SearchedNewsPagingAdapter(
            onNewsClicked = { clickedNews ->
                findNavController().navigate(
                    SearchNewsFragmentDirections.openNewsDetailScreenFromSearchNewsScreen(
                        newsItem = clickedNews,
                        navigateFromBookmarkedScreen = false
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
                viewModel.clearUiState()
            }
        }
    }

    private fun searchNews() {
        val searchQuery = binding.searchNewsInputLayout.editTextValue
        if (searchQuery.isNotEmpty()) {
            viewModel.updateHeaderTitleTextState(text = null)
            viewModel.findNewsByQuery()
            binding.searchNewsInputLayout.editText?.hideKeyboard()
        } else {
            toast(text = getString(R.string.empty_search_text_message))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::callback.isInitialized) {
            callback.remove()
        }
    }
}