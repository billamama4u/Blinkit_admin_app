plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.tarun.admin_blinkit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tarun.admin_blinkit"
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
    buildFeatures{
        viewBinding=true
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

    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}