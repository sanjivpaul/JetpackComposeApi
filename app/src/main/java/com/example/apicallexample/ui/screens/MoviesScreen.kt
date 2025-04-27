package com.example.apicallexample.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apicallexample.data.model.Movie
import com.example.apicallexample.viewmodel.MoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen (navController: NavController, viewModel: MoviesViewModel= hiltViewModel()){

    //    states of movies
    val movies by viewModel.movies
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val isLastPage by viewModel.isLastPage

    var searchQuery by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    // Status bar control
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

//    pull to refresh state
    // Pull-to-refresh state - connect to ViewModel's loading state
    val isRefreshing by remember { derivedStateOf { isLoading && viewModel.currentPage == 1 }}
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)


    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, // Or any color you want
            darkIcons = useDarkIcons
        )
    }

//    handle pagination when scrolling
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index == layoutInfo.totalItemsCount - 1
            }
            .distinctUntilChanged()
            .collect{reachedEnd ->
                if(reachedEnd && !isLoading && !isLastPage && searchQuery.isEmpty()){
                    viewModel.loadNextPage()
                }

            }
    }

    // Filter movies based on search query
    val filteredMovies = remember (movies, searchQuery){
        if(searchQuery.isEmpty()){
            movies
        }else{
            movies.filter { movie ->
                movie.title.contains(searchQuery, ignoreCase = true) ||
                        movie.overview.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),

//                loading indicator
                actions = {
                    if(isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(34.dp)
                                .padding(end =16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            )
        },
        content = { innerPadding ->

//            wrapping Column into SwipeRefresh to perform pull to refresh
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refresh() }, // Let ViewModel handle the refresh
            ) {
                Column(modifier = Modifier.padding(innerPadding)) {
//                Show Error
                    error?.let { errorMessage ->
                        Text(
                            text = "Error: ${errorMessage}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )

                        Button(
                            onClick = {viewModel.clearError()},
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Retry")
                        }
                    }

                    //            Search bar
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = {searchQuery = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    if (filteredMovies.isEmpty() && !isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No movies found")
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 16.dp), // Add horizontal padding
                        state = lazyListState // for reload and pagination

                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(top = 8.dp)) // Add top spacer
                        }

//                here replace movies data with filtered data
                        items(filteredMovies) { movie ->
                            MovieItem(
                                movie = movie,
                                onClick = { navController.navigate("movie_details/${movie.id}") }
                            )
                            Spacer(modifier = Modifier.padding(vertical = 8.dp)) // Add spacing between items
                        }

//                    show loading indicator at the bottom when loading more
                        if(isLoading && filteredMovies.isNotEmpty()){
                            item{
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        // Show end of list message
                        if (isLastPage && filteredMovies.isNotEmpty()) {
                            item {
                                Text(
                                    text = "No more movies to load",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }



        }
    )

}

@Composable
fun MovieItem(movie: Movie, onClick: ()->Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )
    {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        )
            {
                AsyncImage(
        //            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                    model = movie.poster_path,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                        .clip(MaterialTheme.shapes.small),

                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f))
                    {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier=Modifier
    ){
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier=modifier,
        placeholder = {Text("Search movies...")},
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        singleLine = true,
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            // This removes the underline
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.large,
        )
}


// search by api next practice
