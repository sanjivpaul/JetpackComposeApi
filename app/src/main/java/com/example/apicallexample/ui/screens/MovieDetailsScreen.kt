package com.example.apicallexample.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.apicallexample.data.model.Genre
import com.example.apicallexample.viewmodel.MoviesViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MovieDetailsScreen(movieId:Int, viewModel: MoviesViewModel= hiltViewModel()) {
    val movieDetails by remember { viewModel.movieDetails }

    // Status bar control
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

//    Scaffold use for add system bars properly
    Scaffold { innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding) // This handles system bar insets
                .padding(16.dp) // Add your content padding
                .verticalScroll(rememberScrollState()) // Make content scrollable
        )
        {

            movieDetails?.let { details ->
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Movie title
                    Text(
                        text = details.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
//                model = "https://image.tmdb.org/t/p/w500${details.poster_path}",
                        model = details.poster_path,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Metadata row
                    Row(
                        modifier = Modifier.padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val runtimeString = details.runtime?.split(" ")?.firstOrNull() ?: "0"

                        Text(
                            text = "Released: ${details.release_date}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${details.runtime}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Rating
//                    Text(
//                        text = "Rating: ${details.vote_average}/10",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    )

                    StarRating(
                        rating = details.vote_average,
                    )

                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    GenreListView(details.genres)

                    // Overview
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = details.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }

        }

    }

}


// Add this function to create star rating
@Composable
fun StarRating(rating: Float, maxStars: Int = 5) {
    val filledStars = (rating / 2).toInt() // Convert 10-point scale to 5 stars
    val unfilledStars = maxStars - filledStars

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Filled stars
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Filled star",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
        }

        // Unfilled stars
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Unfilled star",
                tint = Color(0xFFAAAAAA), // Gray color
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
        }

        // Rating text
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${rating}/10",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
    }
}

@Composable
fun StringListView(strings: List<String>) {
    Column {
        strings.forEach { str ->
            Text(text = str)
        }
    }
}

@Composable
fun GenreListView(genres: List<Genre>){
    Row(modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {

//        map genres and display
        genres.forEachIndexed { index, genre ->
            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyMedium
            )

//            if genre is not present
            if(index < genres.size -1){
                Text(
                    text = " . ",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

