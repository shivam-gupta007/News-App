package com.shivamgupta.newsapp.presentation.bookmark_news

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import com.shivamgupta.newsapp.domain.models.News
import com.shivamgupta.newsapp.domain.models.asBookmarkedNewsEntity
import com.shivamgupta.newsapp.domain.models.asNews
import com.shivamgupta.newsapp.domain.repository.BookmarkNewsRepository
import com.shivamgupta.newsapp.util.ExceptionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkNewsViewModel @Inject constructor(
    private val repository: BookmarkNewsRepository,
    private val app: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarkNewsUiState())
    val uiState get() = _uiState.asStateFlow()

    fun getBookmarkedNews() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            repository.getBookmarkedNews().catch {
                _uiState.update { currentUiState ->
                    val message = ExceptionUtils.getUiMessageOfException(
                        throwable = it,
                        context = app.applicationContext
                    )
                    currentUiState.copy(
                        userMessage = message,
                        isLoading = false
                    )
                }
            }.collect { localNews ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        news = localNews.map(BookmarkedNewsEntity::asNews), isLoading = false
                    )
                }
            }
        }
    }

    fun addNews(news: News){
        viewModelScope.launch {
            repository.addOrIgnoreNews(news.asBookmarkedNewsEntity())
        }
    }

    fun deleteBookmarkedNews(newsId: Long){
       viewModelScope.launch {
           repository.deleteNews(newsId)
       }
    }

    fun userMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = null)
        }
    }

}