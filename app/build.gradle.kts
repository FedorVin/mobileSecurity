plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.mobileSec.pomodorotodo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobileSec.pomodorotodo"
        minSdk = 24
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

    buildFeatures {
        compose = true
    }

    // compileOptions { // Not needed if only using Kotlin, but doesn't harm
    //     sourceCompatibility = JavaVersion.VERSION_1_8
    //     targetCompatibility = JavaVersion.VERSION_1_8
    // }
    kotlinOptions {
        jvmTarget = "1.8" // This is fine for now, but with Kotlin 2.1.10 you could target higher JVM if desired
    }
}

dependencies {
    // ... your dependencies are mostly fine, but double check versions mentioned in toml comments ...
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("javax.inject:javax.inject:1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    // Room compiler (required for Room to work)
    implementation("androidx.room:room-ktx:2.6.1")

    // Compose UI (required for Jetpack Compose)
    implementation("androidx.compose.ui:ui:1.5.4") // These versions are for older Compose.
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4") // Consider using a Compose BOM
    implementation("androidx.activity:activity-compose:1.8.2") // to align all compose versions

    // Lifecycle & Coroutines
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Location services (for malware functionality)
    implementation("com.google.android.gms:play-services-location:21.0.1")
}