package com.ritesh.newsfeed

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun splashScreen_displaysInitialContent() {
        // 1. Verify the main title is visible
        composeTestRule
            .onNodeWithText("News Today")
            .assertIsDisplayed()

        // 2. Verify the version number is visible at the bottom
        composeTestRule
            .onNodeWithText("Version 1.0.0")
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun splashScreen_navigatesToHomeAfterDelay() {
        // 1. Ensure we start on the Splash Screen
        composeTestRule.onNodeWithText("News Today").assertIsDisplayed()

        // 2. Wait for the Splash Screen to navigate away.
        // We wait for the "News Today" text to disappear from the hierarchy
        composeTestRule.waitUntilDoesNotExist(
            hasText("News Today"),
            timeoutMillis = 3000 // Giving it extra time beyond the 1000ms delay
        )

        // 3. Verify that we have landed on the Home Screen (assuming it has "News Feed")
        composeTestRule
            .onNodeWithText("News Feed", ignoreCase = true)
            .assertIsDisplayed()
    }
}