package com.example.apicallexample.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apicallexample.ui.screens.MovieDetailsScreen
import com.example.apicallexample.ui.screens.MoviesScreen

// navigation/NavigationGraph.kt
fun NavGraphBuilder.moviesGraph(navController: NavController) {
    composable(NavItems.MoviesList.route) {
        MoviesScreen(navController = navController)
    }
    composable(
        route = NavItems.MovieDetails.route,
        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        )
        { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetailsScreen(movieId = movieId)
    }
}