package com.example.milobeats

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milobeats.data.api.VideoItem
import com.example.milobeats.presentation.components.VideoPlayer
import com.example.milobeats.presentation.viewmodel.YouTubePlayerViewModel
import com.example.milobeats.ui.theme.MiloBeatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiloBeatsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: YouTubePlayerViewModel = hiltViewModel()
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: YouTubePlayerViewModel) {
    var currentScreen by remember { mutableIntStateOf(0) }
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle(initialValue = "")
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedVideo by viewModel.selectedVideo.collectAsStateWithLifecycle(initialValue = null)
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle(initialValue = false)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(currentScreen, listOf("Search", "Play", "Home")) { index ->
                currentScreen = index
            }
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                0 -> { // Search screen
                    SearchScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { query ->
                            viewModel.updateSearchQuery(query)
                            viewModel.searchVideos(query)
                        },
                        onSearch = { query -> viewModel.searchVideos(query) },
                        searchResults = searchResults,
                        onVideoClick = { video ->
                            viewModel.selectVideo(video)
                            currentScreen = 1
                        }
                    )
                }
                1 -> { // Play screen
                    selectedVideo?.let { video ->
                        PlayScreen(
                            video = video,
                            isPlaying = isPlaying,
                            onPlayPause = viewModel::togglePlayback
                        )
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No video selected",
                                color = Color.White
                            )
                        }
                    }
                }
                2 -> { // Home screen
                    HomeScreen(
                        videos = searchResults,
                        isLoading = false,
                        error = null,
                        onSearch = viewModel::searchVideos,
                        selectedVideo = selectedVideo,
                        onVideoSelected = { video ->
                            viewModel.selectVideo(video)
                            currentScreen = 1
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    videos: List<VideoItem>,
    isLoading: Boolean,
    error: String?,
    onSearch: (String) -> Unit,
    selectedVideo: VideoItem?,
    onVideoSelected: (VideoItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF004D40),
                        Color(0xFF263238)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Home Screen", color = Color.White)
        }
    }
}

@Composable
fun TopAppBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* Handle back */ }) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { /* Handle more options */ }) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More Options",
                tint = Color.White
            )
        }
    }
}

@Composable
fun PlayScreen(
    video: VideoItem,
    isPlaying: Boolean,
    onPlayPause: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = video.snippet.title)

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoPlayer(
                    videoId = video.id.videoId,
                    modifier = Modifier.weight(0.4f)
                )
                Column(
                    modifier = Modifier.weight(0.6f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    VideoDetailsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .padding(horizontal = 16.dp),
                        video = video
                    )
                    PlaybackControlsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .padding(bottom = 16.dp),
                        isPlaying = isPlaying,
                        onPlayPause = onPlayPause
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                VideoPlayer(
                    videoId = video.id.videoId,
                    modifier = Modifier.weight(0.6f)
                )
                VideoDetailsSection(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(0.2f),
                    video = video
                )
                PlaybackControlsSection(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .weight(0.2f),
                    isPlaying = isPlaying,
                    onPlayPause = onPlayPause
                )
            }
        }
    }
}

@Composable
fun VideoThumbnailSection(
    modifier: Modifier = Modifier,
    thumbnailUrl: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            error = painterResource(id = R.drawable.ic_launcher_foreground)
        )
    }
}

@Composable
fun VideoDetailsSection(
    modifier: Modifier = Modifier,
    video: VideoItem
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = video.snippet.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = video.snippet.channelTitle,
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }
            Row {
                IconButton(onClick = { /* Handle subtract */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_delete),
                        contentDescription = "Subtract",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* Handle add */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PlaybackControlsSection(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlayPause: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle previous */ }) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(160.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.Black,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
            )
        }
        IconButton(onClick = { /* Handle next */ }) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    items: List<String>,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = Color.Black) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    val icon: ImageVector = when (item) {
                        "Home" -> Icons.Outlined.Home
                        "Search" -> Icons.Outlined.Search
                        "Play" -> Icons.Default.PlayArrow
                        else -> Icons.Outlined.Home // Fallback
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = item,
                        tint = if (selectedItem == index) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        item,
                        color = if (selectedItem == index) Color.White else Color.Gray
                    )
                },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

@Composable
fun SearchScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<VideoItem>,
    onVideoClick: (VideoItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Gray)
            )
            Text(
                text = "Search",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SearchBarComposable(
            searchQuery = searchQuery,
            onValueChange = onSearchQueryChange,
            onSearch = onSearch
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (searchResults.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { video ->
                    VideoItem(video = video, onVideoSelected = onVideoClick)
                }
            }
        } else if (searchQuery.isEmpty()) {
            Text(
                text = "Start browsing",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        } else {
            Text(
                text = "No videos found",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun SearchBarComposable(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Icon",
                tint = Color.Black
            )
        },
        placeholder = {
            Text("What do you want to listen to?", color = Color.Gray)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchQuery)
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun VideoItem(
    video: VideoItem,
    onVideoSelected: (VideoItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onVideoSelected(video) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(video.snippet.thumbnails.medium.url)
                .crossfade(true)
                .build(),
            contentDescription = "Video Thumbnail",
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_launcher_foreground)
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = video.snippet.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = video.snippet.channelTitle,
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MiloBeatsTheme {
        HomeScreen(
            videos = emptyList(),
            isLoading = true,
            error = null,
            onSearch = { _ -> },
            selectedVideo = null,
            onVideoSelected = { _ -> }
        )
    }
}