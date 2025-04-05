plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.csci3310"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.csci3310"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"

    }

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)

        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)

        implementation(platform("androidx.compose:compose-bom:2024.12.01"))
        implementation(libs.androidx.scenecore)
        implementation(libs.androidx.navigation.compose)
        debugImplementation("androidx.compose.ui:ui-tooling")

        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.constraintlayout)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("com.airbnb.android:lottie-compose:6.0.0")

        implementation("com.google.accompanist:accompanist-permissions:0.31.2-alpha")
        implementation("androidx.camera:camera-camera2:1.2.3")
        implementation("androidx.camera:camera-lifecycle:1.2.3")
        implementation("androidx.camera:camera-view:1.2.3")
        implementation("com.google.guava:guava:31.1-android")
        implementation("com.google.mlkit:face-detection:16.1.5")
        implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

        implementation("androidx.activity:activity-compose:1.10.1")
        implementation("androidx.compose.ui:ui-graphics:1.7.8")
        implementation("androidx.compose.ui:ui:1.7.8")
        implementation("androidx.compose.ui:ui-tooling:1.7.8")
        implementation("androidx.compose.material3:material3:1.3.1")

        implementation("androidx.navigation:navigation-compose:2.8.9")

    
        implementation ("io.coil-kt:coil-compose:2.4.0")

    }
}
dependencies {
    implementation(libs.volley)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.espresso.core)
}
