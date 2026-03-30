package com.ritesh.newsfeed.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ritesh.newsfeed.data.model.Article
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.ritesh.newsfeed.presentation.NewsArticleEvents.FetchNewsArticle
import com.ritesh.newsfeed.presentation.NewsArticleEvents.OnApplyNewUpdates
import com.ritesh.newsfeed.presentation.NewsArticleViewModel
import com.ritesh.newsfeed.ui.theme.Primary
import com.ritesh.newsfeed.ui.theme.Secondary
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.ritesh.newsfeed.presentation.screens.Toolbar
import com.ritesh.newsfeed.ui.theme.TextBlack
import com.ritesh.newsfeed.ui.theme.TextGray
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    country: String?
) {
    val articleViewMode = hiltViewModel<NewsArticleViewModel>()
    val state by articleViewMode.articleState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-hide the "New Updates" icon if user manually scrolls to the top
    val isAtTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    LaunchedEffect(country) {
        country?.let {
            articleViewMode.doAction(FetchNewsArticle(it, 1))
        }
    }

    LaunchedEffect(isAtTop) {
        if (isAtTop && state.hasNewUpdates) {
            articleViewMode.doAction(OnApplyNewUpdates)
        }
    }

    Scaffold(
        containerColor = Secondary,
        topBar = {
            Toolbar(
                showAboutOption = true,
                showBackOption = false,
                title = "News Feed",
                onAboutButtonClicked = {},
                onBackButtonClicked = {}
            )
        }
    ) { innerPadding ->
        val padding = innerPadding.calculateTopPadding()
        Box(
            modifier = modifier
                .padding(top = 64.dp)
                .background(Secondary)
        ) {

            if (state.isLoading) {
                LoadingScreen(modifier)
            }

            state.error?.let {
                ErrorMessage(message = state.error ?: "Something went wrong")
            }

            if (!state.displayedArticles.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState
                ) {
                    items(
                        items = state.displayedArticles,
                        key = { "temp_key${it.title + it.publishedAt}" }
                    ) {
                        NewsArticle(newsArticle = it)
                    }
                }


                // The "New Updates" Button
                if (state.hasNewUpdates && !isAtTop) {
                    Button(
                        onClick = {
                            scope.launch {
                                articleViewMode.doAction(OnApplyNewUpdates)
                                listState.animateScrollToItem(0) // Scroll to top
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("New Updates Available")
                    }
                }

            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .align(Alignment.Center),
            color = Primary,
            strokeWidth = 4.dp
        )
    }
}

@Preview
@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String = "Something went wrong",
    onRetry: () -> Unit = {}
) {
    val onRetryClicked by rememberUpdatedState(onRetry)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        Button(onClick = onRetryClicked) {
            Text("Retry")
        }
    }
}

@Composable
fun NewsArticles(articles: List<Article>) {
    if (articles.isEmpty()) ErrorMessage(message = "No news found")


}

@Preview
@Composable
fun NewsArticle(modifier: Modifier = Modifier, newsArticle: Article? = null) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary),
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .padding(
                        bottom = 16.dp,
                        end = 16.dp
                    )
                    .width(150.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(100.dp),
                    model = newsArticle?.urlToImage,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    text = newsArticle?.source?.name ?: "Source Unknown",
                    style = TextStyle(
                        color = TextGray,
                        textDirection = TextDirection.Ltr,
                        fontSize = 12.sp,
                        fontWeight = Normal
                    ),
                    maxLines = 1
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = newsArticle?.title ?: "Author",
                    style = TextStyle(
                        color = TextBlack,
                        textDirection = TextDirection.Ltr,
                        fontSize = 22.sp,
                        fontWeight = Bold
                    ),
                    maxLines = 2
                )
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    text = newsArticle?.description ?: "Title",
                    style = TextStyle(
                        color = TextBlack,
                        textDirection = TextDirection.Ltr,
                        fontSize = 16.sp,
                        fontWeight = Normal
                    ),
                    maxLines = 4
                )
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    text = newsArticle?.publishedAt ?: "Published At",
                    style = TextStyle(
                        color = TextGray,
                        textDirection = TextDirection.Ltr,
                        fontSize = 12.sp,
                        fontWeight = Normal
                    )
                )
            }
        }
    }
}