package com.ritesh.newsfeed.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ritesh.newsfeed.presentation.NewsArticleViewModel
import com.ritesh.newsfeed.presentation.screens.home.HomeScreen
import com.ritesh.newsfeed.presentation.screens.splash.SplashScreen

@Composable
fun NavScreen(modifier: Modifier) {
    val navController = rememberNavController()
    val splash = NewsFeedScreens.SplashScreen.name

    NavHost(
        navController = navController,
        startDestination = splash
    ) {

        val home = NewsFeedScreens.HomeScreen.name


        composable(splash) {
            SplashScreen(navController, modifier)
        }

        composable("$home/{country}",
            arguments = listOf(
                navArgument(name = "country"){
                    type = StringType
                })
        ) { backStackEntry ->
            val country = backStackEntry.arguments?.getString("country")

            HomeScreen(modifier, navController, country)
        }

    }
}