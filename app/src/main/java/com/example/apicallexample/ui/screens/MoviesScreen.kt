package com.example.apicallexample.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apicallexample.data.model.Movie
import com.example.apicallexample.viewmodel.MoviesViewModel

@Composable
fun MoviesScreen (navController: NavController, viewModel: MoviesViewModel= hiltViewModel()){
    val movies by viewModel.movies
//    Text(text = "Hello, Movies!")


    LazyColumn(modifier = Modifier.padding(10.dp)) {

        items(movies){movie ->

            MovieItem(movie = movie, onClick={
                navController.navigate("movie_details/${movie.id}")
            })
        }
    }

}

@Composable
fun MovieItem(movie: Movie, onClick: ()->Unit){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text=movie.title, style = MaterialTheme.typography.titleLarge)
            Text(text = movie.overview, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }

}