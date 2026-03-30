package com.ritesh.newsfeed.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ritesh.newsfeed.ui.theme.Primary
import com.ritesh.newsfeed.ui.theme.TextBlack


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    showAboutOption: Boolean = false,
    showBackOption: Boolean = false,
    title: String = "About Device",
    onAboutButtonClicked: () -> Unit = {},
    onBackButtonClicked: () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary,
            titleContentColor = TextBlack,
            navigationIconContentColor = TextBlack,
            actionIconContentColor = TextBlack
        ),
        title = {
            Text(
                text = title
            )
        },
        actions = {
            if (showAboutOption) {
                IconButton(
                    onClick = onAboutButtonClicked
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "About Device Button"
                    )
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "About Device Button"
                    )
                }
            }

        },
        navigationIcon = {
            if (showBackOption) {
                IconButton(
                    onClick = onBackButtonClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}