package com.ritesh.newsfeed.presentation

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ritesh.newsfeed.MainDispatcherRule
import com.ritesh.newsfeed.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class NewsArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private lateinit var getNewsUseCase: GetNewsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: NewsArticleViewModel

    @Before
    fun setup() {
        getNewsUseCase = mock(GetNewsUseCase::class.java)
        savedStateHandle = SavedStateHandle(mapOf("current_country" to "us"))
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Mock the use case to return a flow that never emits (simulating infinite loading)
        `when`(getNewsUseCase.execute("us", 0, false)).thenReturn(flow { })

        viewModel = NewsArticleViewModel(getNewsUseCase, savedStateHandle)

        assertThat(viewModel.articleState.value.isLoading).isTrue()
    }

}