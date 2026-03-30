package com.ritesh.newsfeed.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ritesh.newsfeed.presentation.screens.NewsFeedScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(appNav: NavController, modifier: Modifier) {

    LaunchedEffect(key1 = true) {
        delay(1000)
        appNav.navigate(NewsFeedScreens.HomeScreen.name + "/us") {
            popUpTo(NewsFeedScreens.HomeScreen.name) {
                inclusive = true
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Text(
            text = "News Today",
            modifier = modifier
                .align(Alignment.Center)
                .padding(16.dp),
            fontSize = 32.sp,
            color = Color.Blue
        )

        Text(
            text = "Version 1.0.0",
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            fontSize = 12.sp,
            color = Color.Blue
        )
    }
}