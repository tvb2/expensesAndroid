plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("org.jetbrains.kotlin.plugin.compose")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.expensescontrol"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.expensescontrol"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation(platform(libs.androidx.compose.bom.v20241201))
    implementation(libs.androidx.activity.compose)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kmp.date.time.picker)
    implementation(libs.protolite.well.known.types)
    implementation(project(":sqlitecloud"))
    implementation(libs.androidx.runner)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core)
    implementation(libs.core)
    implementation(libs.espresso.core)
    implementation(libs.material)
    implementation(libs.androidx.core)
    testImplementation(libs.junit.junit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.json)
    androidTestImplementation(libs.androidx.monitor)
    ksp(libs.androidx.room.compiler)

}