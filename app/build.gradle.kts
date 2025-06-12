plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.tasananew"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tasananew"
        minSdk = 31
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
    }
}

dependencies {
    // Core & lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)



    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Room
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.runtime.livedata)
    kapt("androidx.room:room-compiler:2.7.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // LiveData <-> Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // ViewModel untuk Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // CameraX core & Compose
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    implementation("androidx.camera:camera-extensions:1.4.2")
    implementation("androidx.camera:camera-compose:1.5.0-beta01") // preview via Compose :contentReference[oaicite:5]{index=5}
    implementation ("androidx.activity:activity-compose:1.7.2")

    // CameraX PreviewView (gunakan AndroidView)
    implementation("androidx.camera:camera-view:1.3.1")

    // Compose Material core
    implementation ("androidx.compose.material:material:<compose_version>")

    // Material Icons Extended (untuk Icons.Filled dan sejenisnya)
    implementation ("androidx.compose.material:material-icons-extended:<compose_version>")
}

