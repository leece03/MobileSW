plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    //id("com.android.application")
    alias(libs.plugins.google.secrets)
    alias(libs.plugins.google.services)
    //id("com.google.gms.google-services")
}

android {
    namespace = "com.example.re0"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.re0"
        minSdk = 24
        targetSdk = 36
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
    //kotlinOptions {
    //    jvmTarget = "11"
    //}
    buildFeatures {
        buildConfig = true
        compose = true
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.compose.material:material-icons-extended:<version>")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    //implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Lifecycle /Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation(Compose)
    implementation(libs.androidx.navigation.compose)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    //implementation("com.google.dagger:dagger-compiler:2.51.1")
    ksp("com.google.dagger:dagger-compiler:2.51.1")
    implementation(libs.androidx.room.ktx)

    // Hilt  dependencies
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.lifecycle.vm.compose)

    // DataStore (Preferences)
    implementation(libs.androidx.datastore.preferences)

    // Maps Compose 의존성 추가
    implementation(libs.maps.compose)
// 현재 위치(FusedLocation)용
    implementation(libs.play.services.location)
    //implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    //implementation("com.google.firebase:firebase-analytics")
}
hilt {
    enableAggregatingTask = false
}