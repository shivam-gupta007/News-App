package com.shivamgupta.newsapp.presentation.search_news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.shivamgupta.newsapp.domain.models.asNews
import com.shivamgupta.newsapp.domain.repository.NewsRepository
import com.shivamgupta.newsapp.util.truncateStringWithDots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchNewsUiState())
    val uiState get() = _uiState.asStateFlow()

    private var searchNewsJob: Job? = null

    fun searchNews() {
        searchNewsJob?.cancel()
        searchNewsJob = viewModelScope.launch {
            val searchQuery = _uiState.value.searchedText.orEmpty()
            repository.searchNews(searchQuery).cachedIn(viewModelScope).collectLatest {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            headerTitleText = "Search results for ${searchQuery.truncateStringWithDots(20)}",
                            searchedNews = it.map { it.asNews() }
                        )
                    }
                }
        }
    }

    fun updateSearchTextState(text: String?){
        _uiState.update { currentUiState->
            currentUiState.copy(searchedText = text)
        }
    }

    fun updateHeaderTitleTextState(text: String?){
        _uiState.update { currentUiState->
            currentUiState.copy(headerTitleText = text)
        }
    }

    fun clearUiState(){
        _uiState.update {
            SearchNewsUiState()
        }
    }
}