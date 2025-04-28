package com.example.apicallexample


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apicallexample.navigation.AppNavigation
import com.example.apicallexample.ui.screens.MovieDetailsScreen
import com.example.apicallexample.ui.screens.MoviesScreen
import com.example.apicallexample.ui.theme.ApiCallExampleTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp{
//                MoviesNavGraph()
                AppNavigation()
            }
        }
    }
}

//@Composable
//fun MoviesNavGraph(startDestination: String="movies_list"){
//    val navController= rememberNavController()
//
//    NavHost(navController = navController, startDestination = startDestination) {
//        composable("movies_list") {
//            MoviesScreen(navController = navController)
//        }
//        composable("movie_details/{movieId}",
//            arguments = listOf(navArgument("movieId"){type = NavType.IntType})
//        ){ backStackEntry ->
//            val movieId =backStackEntry.arguments?.getInt("movieId")?:return@composable
//            MovieDetailsScreen(movieId=movieId)
//
//        }
//    }
//
//}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    ApiCallExampleTheme {
        content()
    }
}

