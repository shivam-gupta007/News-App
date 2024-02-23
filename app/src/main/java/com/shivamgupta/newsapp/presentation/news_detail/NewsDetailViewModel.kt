package com.shivamgupta.newsapp.presentation.news_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.newsapp.domain.models.News
import com.shivamgupta.newsapp.domain.models.asNews
import com.shivamgupta.newsapp.domain.repository.BookmarkNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val bookmarkNewsRepository: BookmarkNewsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(NewsDetailUiState())
    val uiState get() = _uiState.asStateFlow()

    fun updateNewsDetailState(newsItem: News){
        _uiState.update { currentUiState ->
            currentUiState.copy(newsDetail = newsItem)
        }
    }

    /**
     * Maps the bookmark status of a news item by fetching news based on its title.
     *
     * This method asynchronously retrieves the bookmarked news from the repository
     * using the provided news item's title. If bookmarked news is found, it updates
     * the UI state with the bookmarked news; otherwise, it uses the original news item.
     *
     * @param newsItem The news item for which to map the bookmark status.
     */
    fun mapBookmarkStatusOfNewsByTitle(newsItem: News){
        viewModelScope.launch {
            val bookmarkedNews = bookmarkNewsRepository.getBookmarkedNewsByTitle(newsItem.title)
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    newsDetail = bookmarkedNews?.asNews() ?: newsItem
                )
            }
        }
    }

    fun updateBookmarkStatusState(bookmarked: Boolean){
        _uiState.update { currentUiState ->
            val updatedNews = currentUiState.newsDetail?.copy(isBookmarked = bookmarked)
            currentUiState.copy(newsDetail = updatedNews)
        }
    }

    fun clearUiState(){
        _uiState.update {
            it.copy(newsDetail = null)
        }
    }
}