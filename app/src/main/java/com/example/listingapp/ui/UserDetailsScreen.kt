@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.listingapp.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.listingapp.R
import com.example.listingapp.data.local.UserEntity
import com.example.listingapp.data.model.WeatherResponse
import com.example.listingapp.viewmodel.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun UserDetailsScreen(
    navController: NavController,
    userId: String,
    viewModel: UserViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val weather by viewModel.weather.collectAsStateWithLifecycle()

    val user = users.find { it.id == userId }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        isVisible = true
        user?.let { viewModel.getWeatherForUser(it) }
    }

    Scaffold(
        topBar = { HeaderDetail(navController) },
        containerColor = Color(0xFFF2F5FA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.primary_first),
                            colorResource(id = R.color.primary_second)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (user == null) {
                Text(text = "User Not Found", style = MaterialTheme.typography.headlineSmall)
            } else {
                AnimatedVisibility(visible = isVisible) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileSection(user)
                        Spacer(modifier = Modifier.height(24.dp))
                        UserInfoSection(user)
                        Spacer(modifier = Modifier.height(4.dp))
                        WeatherInfoSection(weather)
                        Spacer(modifier = Modifier.height(12.dp))
                        WeatherDetailsSection(weather)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderDetail(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "User Details",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 22.sp,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = colorResource(id = R.color.primary_first),),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProfileSection(user: UserEntity) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = user.profileImage,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.cludy)
        )
    }
}

@Composable
fun UserInfoSection(user: UserEntity) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = user.email, fontSize = 16.sp, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = user.location, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        CurrentDateText();
    }
}

@SuppressLint("NewApi")
@Composable
fun CurrentDateText() {
    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, MMM d", Locale.ENGLISH))
    }

    Text(text = currentDate, fontSize = 14.sp, color = Color.White)
}

@Composable
fun WeatherInfoSection(weather: WeatherResponse?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (weather != null) {
            AsyncImage(
                model = R.drawable.cludy,
                contentDescription = "Weather Icon",
                modifier = Modifier.size(140.dp)
            )
            Text(
                text = "${weather.main.temp}°C",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.offset(y = (-20).dp)
            )
            Text(
                text = weather.weather[0].description.capitalize(),
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.offset(y = (-10).dp)
            )
        } else {
            Text(text = "Loading weather...", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

@Composable
fun WeatherDetailsSection(weather: WeatherResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weather != null) {
            WeatherDetailItem(icon = R.drawable.drop, label = "Rainfall", value = getRainfall(weather))
            WeatherDetailItem(icon = R.drawable.wind, label = "Wind",  value = "${weather.wind.speed} km/h")
            WeatherDetailItem(icon = R.drawable.umbrella, label = "Humidity",  value = "${weather.main.humidity} %")
        }

    }
}

@Composable
fun WeatherDetailItem(icon: Int, label: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.primary_first),),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

fun getRainfall(weather: WeatherResponse): String {
    return if (weather.rain?.h1h != null) {
        "${weather.rain.h1h} mm"
    } else {
        "0 mm"
    }
}

@Preview(showBackground = true, name = "User Detail Preview")
@Composable
fun PreviewUserDetailsScreen() {
    val fakeNavController = rememberNavController()
    UserListScreen(navController = fakeNavController)
}
