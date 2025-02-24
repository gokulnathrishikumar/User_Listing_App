plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt") // Kotlin annotation processing tool
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.listingapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.listingapp"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.firebase.firestore.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Retrofit & Gson (API Calls)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Paging (For efficient large dataset loading)
    implementation(libs.androidx.paging.compose)

    // Google Play Services (Location API)
    implementation(libs.play.services.location)

    // Image Loading (Coil)
    implementation(libs.coil.compose)
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.compiler)

    // Hilt Navigation for Jetpack Compose
    implementation(libs.androidx.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

}