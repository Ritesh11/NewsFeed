package com.ritesh.newsfeed.presentation

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ritesh.newsfeed.MainDispatcherRule
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.model.Source
import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.usecase.GetNewsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private val getNewsUseCase: GetNewsUseCase = mockk()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: NewsArticleViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle(mapOf("current_country" to "us"))
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Mock the use case to return a flow that never emits (simulating infinite loading)
        coEvery { getNewsUseCase.execute(any(), any(), any()) } returns flowOf(Resource.Loading())

        viewModel = NewsArticleViewModel(getNewsUseCase, savedStateHandle)

        assertThat(viewModel.articleState.value.isLoading).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `successful fetch should update displayed articles`() = runTest {
        // Mock success response
        coEvery{getNewsUseCase.execute(any(), any(), any())} returns flowOf(Resource.Success(initialArticles))

        viewModel = NewsArticleViewModel(getNewsUseCase, savedStateHandle)
        // Using backgroundScope to collect StateFlow (required for stateIn(WhileSubscribed))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.articleState.collect()
        }

        val state = viewModel.articleState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.displayedArticles).isEqualTo(initialArticles)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `new updates should show pending articles and hasNewUpdates true`() = runTest {
        // Create a behavior that emits old then new news
        val newsFlow = MutableStateFlow<Resource<List<Article>>>(Resource.Success(initialArticles))
        coEvery{getNewsUseCase.execute(any(), any(), any())} returns newsFlow

        viewModel = NewsArticleViewModel(getNewsUseCase, savedStateHandle)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.articleState.collect()
        }
        // Trigger the update
        newsFlow.value = Resource.Success(newArticles)


        val state = viewModel.articleState.value
        assertThat(state.hasNewUpdates).isTrue()
        assertThat(state.pendingArticles).isEqualTo(newArticles)
        assertThat(state.displayedArticles).isEqualTo(initialArticles)
    }

    private val initialArticles = listOf(Article(
        id = 1,
        author = "John Doe",
        title = "Breaking News",
        content = "Full content here",
        description = "Short summary",
        publishedAt = "2024-01-01T10:00:00Z",
        source = Source("1", "News Network"),
        url = "https://example.com",
        urlToImage = "https://example.com/image.png")
    )
    private val newArticles = listOf(Article(
        id = 2,
        author = "Jane Smith",
        title = "Tech Revolution 2026",
        content = "The world of AI and mobile development is changing...",
        description = "A deep dive into the latest tech trends.",
        publishedAt = "2026-04-07T08:30:00Z", // Updated for your current year!
        source = Source("2", "Tech Chronicles"),
        url = "https://techchronicles.com/ai-2026",
        urlToImage = "https://techchronicles.com/images/ai-trends.jpg",
        fetchedAt = System.currentTimeMillis()
    ))

}