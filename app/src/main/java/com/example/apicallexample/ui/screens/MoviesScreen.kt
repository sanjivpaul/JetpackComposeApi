package com.example.apicallexample.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme.colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apicallexample.data.model.Movie
import com.example.apicallexample.viewmodel.MoviesViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen (navController: NavController, viewModel: MoviesViewModel= hiltViewModel()){
    val movies by viewModel.movies

    // Status bar control
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, // Or any color you want
            darkIcons = useDarkIcons
        )
    }
    Scaffold(
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp) // Add horizontal padding
            ) {
                item {
                    Spacer(modifier = Modifier.padding(top = 8.dp)) // Add top spacer
                }
                items(movies) { movie ->
                    MovieItem(
                        movie = movie,
                        onClick = { navController.navigate("movie_details/${movie.id}") }
                    )
                    Spacer(modifier = Modifier.padding(vertical = 8.dp)) // Add spacing between items
                }
            }
        }
    )


//    LazyColumn(modifier = Modifier.padding(10.dp)) {
//
//        items(movies){movie ->
//
//            MovieItem(movie = movie, onClick={
//                navController.navigate("movie_details/${movie.id}")
//            })
//        }
//    }

}

@Composable
fun MovieItem(movie: Movie, onClick: ()->Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
    Row(
        modifier = Modifier
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
//            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            model = movie.poster_path,
            contentDescription = null,
            modifier = Modifier.size(120.dp)
                .clip(MaterialTheme.shapes.small),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text=movie.title,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

            )
        }
    }

    }

}