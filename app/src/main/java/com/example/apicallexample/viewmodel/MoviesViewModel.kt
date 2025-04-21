package com.example.apicallexample.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicallexample.data.model.Movie
import com.example.apicallexample.data.model.MovieDetailsResponse
import com.example.apicallexample.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository:MoviesRepository
):ViewModel() {
    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies:State<List<Movie>> = _movies

    private val _movieDetails = mutableStateOf<MovieDetailsResponse?>(null)
    val movieDetails:State<MovieDetailsResponse?> = _movieDetails

    init {
        getMovies()
    }

    private fun getMovies(){
        viewModelScope.launch {
            try {
//                val moviesList = repository.getPopularMovies("Bearer YOUR_ACCESS_TOKEN", 1)
                val moviesList = repository.getPopularMovies("your-secret-key-for-auth", 1)
                _movies.value= moviesList

                // Print response
                println("Movies fetched successfully: $moviesList")
                Log.d("MoviesViewModel", "Movies fetched successfully: $moviesList")

            }catch (e:Exception){
                // handle exception
                println("Error fetching movies: ${e.message}")
                Log.e("MoviesViewModel", "Error fetching movies", e)
            }
        }
    }

    fun getMovieDetails(moviewId:Int){
        viewModelScope.launch {
            try {
//                val details = repository.getMovieDetails("Bearer YOUR_ACCESS_TOKEN", moviewId)
                val details = repository.getMovieDetails("your-secret-key-for-auth", moviewId)
                _movieDetails.value = details

                // Print response
                println("Movie details fetched: $details")
                Log.d("MoviesViewModel", "Movie details fetched: $details")
            }catch (e:Exception){
                //handle exception here
                println("Error fetching movie details: ${e.message}")
                Log.e("MoviesViewModel", "Error fetching movie details", e)
            }
        }
    }
}