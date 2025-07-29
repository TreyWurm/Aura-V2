plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {
    namespace = "at.nukular.aura"
    compileSdk = 36

    defaultConfig {
        applicationId = "at.nukular.aura"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    // Android
    implementation(libs.android.x.core)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Arrow
    implementation(libs.kotlin.arrow.core)
    implementation(libs.kotlin.arrow.coroutines)

    // FlexibleAdapter
    implementation(libs.android.flexible.adapter)

    // Hilt
    implementation(libs.android.hilt)
    kapt(libs.android.hilt.kapt)

    // Koin
    implementation(libs.kotlin.koin.android)

    // Ktor
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.json)

    // Timber
    implementation(libs.android.timber)
}