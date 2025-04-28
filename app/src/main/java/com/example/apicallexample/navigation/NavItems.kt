package com.example.apicallexample.navigation

//object NavItems {
//    const val MOVIES_LIST = "movies_list"
//    const val MOVIE_DETAILS = "movie_details/{movieId}"
//}


// navigation/NavRoutes.kt
sealed class NavItems(val route: String) {
    object MoviesList : NavItems("movies_list")
    object MovieDetails : NavItems("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
    // Add other routes here
}