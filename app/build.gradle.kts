plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.kspPlugin)
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 31
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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.livedata)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment.ktx)
    implementation(libs.converter.gson)
    implementation(libs.glide)
    annotationProcessor(libs.glidecompiler)
    implementation(libs.gson.gson)
    implementation(libs.koin)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
}