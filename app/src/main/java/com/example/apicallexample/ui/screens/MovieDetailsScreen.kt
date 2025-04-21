package com.example.apicallexample.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.apicallexample.viewmodel.MoviesViewModel

@Composable
fun MovieDetailsScreen(movieId:Int, viewModel: MoviesViewModel= hiltViewModel()) {
    val movieDetails by remember { viewModel.movieDetails }

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    movieDetails?.let { details ->
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = details.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Released: ${details.release_date}")
            Text(text = "Runtime: ${details.runtime} minutes")
            Text(text = "Rating: ${details.vote_average}/10")
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${details.poster_path}",
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = details.overview)
        }
    }



}