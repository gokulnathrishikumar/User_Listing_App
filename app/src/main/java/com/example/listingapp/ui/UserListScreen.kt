package com.example.listingapp.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.listingapp.R
import com.example.listingapp.data.local.UserEntity
import com.example.listingapp.data.model.WeatherResponse
import com.example.listingapp.viewmodel.UserViewModel


@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel(),
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val weather by viewModel.weather.collectAsState()
    val cityName by viewModel.cityName.collectAsState()
    val listState = rememberLazyGridState()

    Log.e("TAG", "UserListScreen: cityName : "+cityName )

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                if (lastVisibleItem >= users.size - 3 && !isLoading) {
                    viewModel.loadNextPage()
                }
            }
    }

        Column(modifier = Modifier.fillMaxSize()) {
        Header(weather = weather, cityName = cityName)
        SearchBar(searchText = searchQuery, onSearchTextChanged = { searchQuery = it })
        val configuration = LocalConfiguration.current
        val columnCount = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 5 else 3

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            val filteredUsers = users.filter { it.name.contains(searchQuery, ignoreCase = true) }
            items(filteredUsers) { user ->
                UserGridItem(user) {
                    navController.navigate("details/${user.id}")
                }
            }
            if (isLoading) {
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun Header(weather: WeatherResponse?, cityName: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.primary_first),
                        colorResource(id = R.color.primary_second)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text("User Listing",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontSize = 22.sp)
        }
        Log.e("TAG", "Header: weather : "+weather )

        Row(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(1.dp), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    if (weather != null) {
                        Text(
                            text = "${weather.main.temp}°C",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "--°C", // Default temperature if weather is null
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                    Text(
                        text = cityName ?: "Unknown City", // Fallback text for cityName
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Image(painter = painterResource(id = R.drawable.cludy), contentDescription = "Weather Icon", modifier = Modifier.size(50.dp))
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused)  colorResource(id = R.color.primary_first) else Color.Gray, label = "Border Animation")

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray) },
        trailingIcon = { if (searchText.isNotEmpty()) {
            IconButton(onClick = { onSearchTextChanged("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear Search", tint = Color.Gray)
            }
        } },
        label = { Text("Search User") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium),
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.primary_first), unfocusedBorderColor = Color.Gray),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun UserGridItem(user: UserEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.profileImage,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(90.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(user.name, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@Preview(showBackground = true, name = "User List Preview")
@Composable
fun PreviewUserListScreen() {
    val fakeNavController = rememberNavController()
    UserListScreen(navController = fakeNavController)
}