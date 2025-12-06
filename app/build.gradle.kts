import java.util.Properties // 👈 1. [필수] 이 줄이 맨 위에 있어야 local.properties를 읽을 수 있습니다.

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
    compileSdk = 36 // 원래 설정 유지 (주의: 프리뷰 버전이라 불안정할 수 있음)
    defaultConfig {
        applicationId = "com.example.re0"
        minSdk = 24
        targetSdk = 36 // 원래 설정 유지
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 👇 2. [추가됨] local.properties에서 키를 읽어오는 로직
        val localProps = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localFile.inputStream().use { localProps.load(it) }
        }

        // Gemini API 키 연결 (BuildConfig)
        val geminiKey = localProps.getProperty("GEMINI_API_KEY") ?: ""
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")

        // Google Maps API 키 연결 (Manifest 병합 오류 방지)
        val mapsKey = localProps.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = mapsKey
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
    implementation("com.google.firebase:firebase-auth-ktx")
    //implementation("com.google.firebase:firebase-firestore-ktx")
    // Gemini Google AI Client SDK
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // Coroutines & ViewModel (비동기 처리를 위해 필요)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")


    implementation("androidx.compose.material:material-icons-extended:1.7.5")

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
    implementation(libs.navigation.compose)
    //implementation(libs.navigation.compose)
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
    ksp(libs.androidx.room.compiler)
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

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.coil-kt:coil-compose:2.6.0")

}
hilt {
    enableAggregatingTask = false
}