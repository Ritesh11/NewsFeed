package com.ritesh.newsfeed.presentation.screens.home

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ritesh.newsfeed.HiltTestActivity
import com.ritesh.newsfeed.ui.theme.NewsFeedTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>() // Use createComposeRule for isolated testing

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun loadingState_displaysCircularProgressIndicator() {
        // We set the content to ONLY the HomeScreen
        composeTestRule.setContent {
            NewsFeedTheme {
                HomeScreen(modifier = Modifier, navController = rememberNavController(), country = "us")
            }
        }

        // Instead of asserting immediately, wait for it to appear
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun errorState_displaysRetryButton() {
        // You would typically mock your ViewModel/Repository here to return an error
        composeTestRule.setContent {
            NewsFeedTheme {
                ErrorMessage(message = "Network Error", onRetry = {})
            }
        }

        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network Error").assertIsDisplayed()
    }

    @Test
    fun toolbar_displaysCorrectTitle() {
        composeTestRule.setContent {
            NewsFeedTheme {
                HomeScreen(modifier = Modifier, navController = rememberNavController(), country = "us")
            }
        }

        composeTestRule.onNodeWithText("News Feed").assertIsDisplayed()
    }
}