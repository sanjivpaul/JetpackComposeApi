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
//    Movie List State
    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies:State<List<Movie>> = _movies

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error : State<String?> = _error

    private val _isLastPage = mutableStateOf(false)
    val isLastPage :State<Boolean> = _isLastPage

    var currentPage = 1

//    Movies Details State
    private val _movieDetails = mutableStateOf<MovieDetailsResponse?>(null)
    val movieDetails:State<MovieDetailsResponse?> = _movieDetails

    private val _isDetailsLoading = mutableStateOf(false)
    val isDetailsLoading: State<Boolean> = _isDetailsLoading

    private val _detailsError = mutableStateOf<String?>(null)
    val detailsError : State<String?> = _detailsError

    init {
//        getMovies() // old model without pagination
        loadMovies()
    }

    private fun loadMovies(){
        if(_isLoading.value || _isLastPage.value) return

        _isLoading.value= true
        _error.value = null

        viewModelScope.launch {
            try {
                val moviesList = repository.getPopularMovies("your-secret-key-for-auth", currentPage)
                _movies.value = if (currentPage == 1){
                    moviesList
                }else{
                    _movies.value + moviesList
                }

//                simple way to detect last page - if we get empty result
                if(moviesList.isEmpty()){
                    _isLastPage.value = true
                }

                Log.d("MoviesViewModel", "Loaded Page $currentPage with ${moviesList.size} movies" )
            }catch (e:Exception){
                _error.value = e.message?:"Unknown error occurred"
                Log.d("MoviesViewModel", "Error loading movies",e)
            }finally {
                _isLoading.value = false

            }
        }
    }

    fun loadNextPage(){
        currentPage++
        loadMovies()
    }

    fun refresh(){
        currentPage = 1
        _isLastPage.value = false
        _movies.value= emptyList()
        loadMovies()
    }

//    private fun getMovies(){
//        viewModelScope.launch {
//            try {
////                val moviesList = repository.getPopularMovies("Bearer YOUR_ACCESS_TOKEN", 1)
//                val moviesList = repository.getPopularMovies("your-secret-key-for-auth", 1)
//                _movies.value= moviesList
//
//                // Print response
//                println("Movies fetched successfully: $moviesList")
//                Log.d("MoviesViewModel", "Movies fetched successfully: $moviesList")
//
//            }catch (e:Exception){
//                // handle exception
//                println("Error fetching movies: ${e.message}")
//                Log.e("MoviesViewModel", "Error fetching movies", e)
//            }
//        }
//    }


    fun getMovieDetails(movieId: Int){
        if(_movieDetails.value?.id == movieId) return

        _isDetailsLoading.value = true
        _detailsError.value = null

        viewModelScope.launch {
            try {
                val details = repository.getMovieDetails("your-secret-key-for-auth", movieId)
                _movieDetails.value= details
                Log.d("MoviesViewModel", "Fetched details for movie $movieId")
            }catch (e:Exception){
                _detailsError.value = e.message?:"Failed to load movie details"
                Log.d("MoviesViewModel", "Error fetching movie details", e)
            }finally {
                _isDetailsLoading.value = false
            }
        }

    }

    fun clearMovieDetails(){
        _movieDetails.value = null
        _detailsError.value = null
    }

    fun clearError(){
        _error.value = null
    }

    fun clearDetailsError() {
        _detailsError.value = null
    }

//    fun getMovieDetails(movieId:Int){
//        Log.e("MoviesViewModel", "movie id is== $movieId", )
//
//        viewModelScope.launch {
//            try {
////                val details = repository.getMovieDetails("Bearer YOUR_ACCESS_TOKEN", moviewId)
//                val details = repository.getMovieDetails("your-secret-key-for-auth", movieId)
//                _movieDetails.value = details
//
//                // Print response
//                println("Movie details fetched: $details")
//                Log.d("MoviesViewModel", "Movie details fetched: $details")
//            }catch (e:Exception){
//                //handle exception here
//                println("Error fetching movie details: ${e.message}")
//                Log.e("MoviesViewModel", "Error fetching movie details", e)
//            }
//        }
//    }
}