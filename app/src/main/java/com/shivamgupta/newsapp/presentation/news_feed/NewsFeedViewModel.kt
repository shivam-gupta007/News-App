package com.shivamgupta.newsapp.presentation.news_feed

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.newsapp.data.local.entities.NewsEntity
import com.shivamgupta.newsapp.domain.models.asNews
import com.shivamgupta.newsapp.domain.models.onError
import com.shivamgupta.newsapp.domain.models.onException
import com.shivamgupta.newsapp.domain.models.onLoading
import com.shivamgupta.newsapp.domain.models.onSuccess
import com.shivamgupta.newsapp.domain.repository.NewsRepository
import com.shivamgupta.newsapp.util.ExceptionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val app: Application,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LatestNewsUiState())
    val uiState get() = _uiState.asStateFlow()

    private var topHeadlinesJob: Job? = null

    fun getTopHeadlines(
        fetchFromRemote: Boolean = false,
        countryCode: String
    ) {
        topHeadlinesJob?.cancel()
        topHeadlinesJob = viewModelScope.launch {
            repository.getTopHeadlines(
                fetchFromRemote,
                countryCode
            ).collect { resource ->
                resource.onSuccess { newsEntities ->
                    _uiState.update {
                        it.copy(news = newsEntities.map(NewsEntity::asNews),)
                    }
                }.onError { errorMessage ->
                    _uiState.update {
                        it.copy(userMessage = errorMessage)
                    }
                }.onException {
                    val message = ExceptionUtils.getUiMessageOfException(
                        throwable = it,
                        context = app.applicationContext
                    )
                    _uiState.update {
                        it.copy(userMessage = message)
                    }
                }.onLoading { loading ->
                    _uiState.update {
                        it.copy(isLoading = loading)
                    }
                }
            }
        }
    }


    fun userMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = null)
        }
    }

}