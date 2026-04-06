package com.ritesh.newsfeed.presentation

import android.util.Log
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.usecase.GetNewsUseCase
import com.ritesh.newsfeed.presentation.NewsArticleEvents.FetchNewsArticle
import com.ritesh.newsfeed.presentation.NewsArticleEvents.OnApplyNewUpdates
import com.ritesh.newsfeed.presentation.NewsArticleEvents.Idle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    companion object {
        private const val KEY_COUNTRY = "current_country"
        private const val KEY_PAGE = "current_page"
    }

    // 1. Reactive Input: Listen for country changes from the handle
    private val _country = savedStateHandle.getStateFlow(KEY_COUNTRY, "us")
    private val _page = savedStateHandle.getStateFlow(KEY_PAGE, 0)

    private val _events = MutableSharedFlow<NewsArticleEvents>()


    @OptIn(ExperimentalCoroutinesApi::class)
    val articleState: StateFlow<ArticleUiState> = combine(
        _country.flatMapLatest { getNewsUseCase.execute(it, _page.value, false) },
        _events.onStart { emit(Idle) } // Initial state to start the flow
    ) { resource, event ->
        // Pair the latest network result with the latest UI event
        Pair(resource, event)
    }
        .scan(ArticleUiState()) { currentState, (resource, event) ->
            // This is where onApplyNewUpdates logic lives now!
            reduce(currentState, resource, event)
        }
        .catch { e ->
            Log.e("ViewModel", e.message ?: "Something went wrong")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArticleUiState(isLoading = true)
        )


    fun doAction(events: NewsArticleEvents) {
        viewModelScope.launch {
            when (events) {
                is FetchNewsArticle -> {
                    savedStateHandle[KEY_COUNTRY] = events.country
                    savedStateHandle[KEY_PAGE] = events.page
                }

                is Idle -> {
                    _events.emit(events)
                }

                is OnApplyNewUpdates -> {
                    _events.emit(events)
                }
            }
        }
    }

    private fun reduce(
        currentState: ArticleUiState,
        resource: Resource<List<Article>>,
        event: NewsArticleEvents
    ): ArticleUiState {
        // Handle the manual button click FIRST
        if (event is OnApplyNewUpdates) {
            return currentState.copy(
                displayedArticles = currentState.pendingArticles,
                pendingArticles = emptyList(),
                hasNewUpdates = false
            )
        }

        // Then handle the Network/Database updates
        return when (resource) {
            is Resource.Loading -> currentState.copy(isLoading = true)
            is Resource.Error -> currentState.copy(isLoading = false, error = resource.message)
            is Resource.Success -> {
                val newData = resource.data ?: emptyList()

                if (currentState.displayedArticles.isEmpty()) {
                    currentState.copy(isLoading = false, displayedArticles = newData)
                } else if (newData.firstOrNull()?.id != currentState.displayedArticles.firstOrNull()?.id) {
                    // This creates the "Pending" state
                    currentState.copy(
                        isLoading = false,
                        pendingArticles = newData,
                        hasNewUpdates = true
                    )
                } else {
                    currentState.copy(isLoading = false)
                }
            }
        }
    }

}