package com.example.apicallexample.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apicallexample.ui.screens.MovieDetailsScreen
import com.example.apicallexample.ui.screens.MoviesScreen


//@Composable
//fun AppNavigation(startDestination: String=NavItems.MOVIES_LIST) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = startDestination){
//        composable(NavItems.MOVIES_LIST){
//            MoviesScreen(navController=navController)
//        }
//        composable(NavItems.MOVIE_DETAILS,
//            arguments = listOf(navArgument("movieId"){type=NavType.IntType})
//        ){
//            backStackEntry ->
//            val movieId = backStackEntry.arguments?.getInt("movieId")?:return@composable
//            MovieDetailsScreen(movieId=movieId)
//        }
//    }
//
//}


//with moviesGraph

@Composable
fun AppNavigation(startDestination: String = NavItems.MoviesList.route) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        moviesGraph(navController)
        // Add other graphs here as your app grows
        // authGraph()
        // profileGraph()
    }
}