plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.appmain"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.appmain"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("androidx.media3:media3-exoplayer:1.0.0") // Adjust version as needed
    implementation ("androidx.media3:media3-ui:1.0.0") // Adjust version as needed
    implementation ("io.coil-kt:coil-compose:1.4.0") // Adjust version as needed
    implementation ("androidx.compose.ui:ui:1.0.1")
    implementation ("androidx.compose.material:material:1.0.1")
    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation ("com.google.accompanist:accompanist-coil:0.14.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.0.1")
    implementation ("androidx.compose.runtime:runtime:1.0.1")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.14.2")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.14.2")
    implementation ("com.google.android.exoplayer:exoplayer:2.18.1")
    implementation ("org.osmdroid:osmdroid-android:6.1.20")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.razorpay:checkout:1.6.40") // Correct Razorpay version')
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.31.0-alpha")
    implementation("com.google.android.gms:play-services-location:19.0.1")
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.material3:material3:1.1.0")
    implementation ("androidx.compose.material:material-icons-extended:1.5.0")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.engage.core)
    implementation(libs.androidx.media3.exoplayer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
