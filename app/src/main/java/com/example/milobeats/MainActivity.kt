package com.example.milobeats

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.milobeats.ui.theme.MiloBeatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MiloBeatsTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Play", "Search", "Home")

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(title = "Sesiones desde la loma")
        },
        bottomBar = {
            BottomNavigationBar(selectedItem, items) { index ->
                selectedItem = index
            }
        },
        containerColor = Color.Transparent // Set to Transparent to allow gradient background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF004D40),
                            Color(0xFF263238)
                        )
                    )
                ) // Apply gradient
                .padding(innerPadding)
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlbumArtSection(modifier = Modifier.weight(0.4f))
                    Column(
                        modifier = Modifier
                            .weight(0.6f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        SongDetailsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.3f)
                                .padding(horizontal = 16.dp)
                        )
                        PlaybackControlsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(bottom = 16.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.2f)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { /* Handle cast */ }) {
                                Icon(
                                    imageVector = Icons.Default.Cast,
                                    contentDescription = "Cast",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = { /* Handle share */ }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    AlbumArtSection(modifier = Modifier.weight(0.6f))
                    SongDetailsSection(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(0.2f)
                    )
                    PlaybackControlsSection(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .weight(0.2f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Handle cast */ }) {
                            Icon(
                                imageVector = Icons.Default.Cast,
                                contentDescription = "Cast",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Handle share */ }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
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
fun AlbumArtSection(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for the album art image
        // You'll replace this with your actual image later
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Using default icon as placeholder
            contentDescription = "Album Art",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .then(if (isLandscape) Modifier.height(180.dp) else Modifier) // Reduced height in landscape
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray) // Placeholder background
        )
    }
}

@Composable
fun SongDetailsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Montón de Estrellas",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Norbert", color = Color.LightGray, fontSize = 16.sp)
            }
            Row {
                IconButton(onClick = { /* Handle subtract */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_delete),
                        contentDescription = "Subtract",
                        tint = Color.White
                    ) // Placeholder
                }
                IconButton(onClick = { /* Handle add */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Add",
                        tint = Color.White
                    ) // Placeholder
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..1f,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.DarkGray
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "3:48", color = Color.LightGray, fontSize = 12.sp)
            Text(text = "-1:48", color = Color.LightGray, fontSize = 12.sp)
        }
    }
}

@Composable
fun PlaybackControlsSection(modifier: Modifier = Modifier) {
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
            onClick = { /* Handle play/pause */ },
            modifier = Modifier.size(160.dp) // Ensure IconButton is large enough
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.Black,
                modifier = Modifier
                    .size(80.dp) // Icon size matches parent IconButton
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MiloBeatsTheme {
        HomeScreen()
    }
}