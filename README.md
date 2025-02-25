# 📱 User Listing App  

## 📖 Overview  
The **User Listing App** is an Android application built using **Kotlin** and **Jetpack Compose**. It provides the following features:  

✅ Fetches and displays a list of users from an external API.  
✅ Allows users to search for specific users by name.  
✅ Shows detailed user information, including location-based weather data.  
✅ Implements pagination for efficient data retrieval.  
✅ Utilizes caching mechanisms to minimize redundant API requests.  
✅ Enhances UI with animations for a seamless user experience.  

## 🏗️ Project Structure  

### 📂 Data Layer  
Manages data retrieval from APIs and local storage using **Room Database**.  

#### 🔑 Key Files:  
- `UserEntity.kt` – Defines the user entity and API response structure.  
- `WeatherRepository.kt` – Manages weather data fetching and storage.  
- `WeatherEntity.kt` – Represents weather data for Room database storage.  

### 🎨 UI Layer  
Handles user interactions and presentation logic using **Jetpack Compose**.  

#### 🔑 Key Files:  
- `UserListScreen.kt` – Displays the list of users with search functionality.  
- `UserDetailScreen.kt` – Shows details of a selected user along with weather information.  
- `SplashScreen.kt` – Displays loading animations for a better user experience.  

### ⚙️ Core Functionality  
Manages main logic, navigation, and utility functions.  

#### 🔑 Key Files:  
- `MainActivity.kt` – Entry point of the application.  
- `Constants.kt` – Stores constant values such as API URLs and keys.  
- `AppNavigator.kt` – Handles navigation between screens.  
- `LocationPermissionHelper.kt` – Manages runtime location permission requests.  

## 🚀 Features & Implementation  

### 📜 User Listing with Pagination  
✔ Fetches user data from an external API.  
✔ Implements pagination to load data efficiently as users scroll.  
✔ Displays user details using **Jetpack Compose** UI components.  

### 🔍 Search Functionality  
✔ Enables users to search for a specific user by name.  
✔ Dynamically filters the displayed list as the user types.  

### 🌍 User Detail & Weather Integration  
✔ Displays selected user details.  
✔ Fetches and displays **weather data** based on the user’s location.  
✔ Implements caching to minimize redundant API calls.  

### 📍 Location Permission Handling  
✔ Dynamically requests **location permissions** from the user.  
✔ Ensures the app functions correctly even if permissions are denied.  

### 💾 Caching Mechanism  
✔ Stores **weather data** locally using **Room Database**.  
✔ Reduces unnecessary API calls by checking for recently available data.  

### 🎨 UI Enhancements  
✔ Implements **smooth animations** for buttons and screen transitions.  
✔ Displays visually appealing **loading and error screens**.  
✔ Optimizes navigation between screens for better user experience.  

## ⚡ Setup & Installation  

### 🔧 Prerequisites  
Ensure you have the following installed:  
- ✅ **Android Studio (latest version)**  
- ✅ **Kotlin**  
- ✅ **Jetpack Compose**  
- ✅ **Room Database**  
- ✅ **Retrofit**  

### 📲 How to Run  
1. **Clone the repository** or extract the ZIP file:  
   git clone https://github.com/your-username/UserListingApp.git

🛠️ Technologies Used
Kotlin 🟣
Jetpack Compose 🎨
Hilt (Dependency Injection) ⚙️
Room Database (Local Storage) 💾
Retrofit (API Integration) 🌐
Coroutines & Flow (Asynchronous Operations) 🚀
JUnit & Espresso (Testing) 🧪
📜 License
This project is licensed under the MIT License – feel free to use and modify!

📧 Contact
For any queries or support, reach out to:
📩 gokulnathrishikumar@gmail.com.com
🔗 LinkedIn Profile - https://www.linkedin.com/in/gokulnath-velmurugan-505801228/
