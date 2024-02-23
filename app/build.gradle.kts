plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinSerialization)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.shivamgupta.newsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shivamgupta.newsapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)

    // Kotlin Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // Room DB
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Nav Graph
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // SwipeToRefresh
    implementation(libs.androidx.swiperefreshlayout)

    // Browser API
    implementation(libs.androidx.browser)

    // Paging
    implementation (libs.androidx.paging.common.ktx)
    implementation (libs.androidx.paging.runtime.ktx)

}