import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

apply(from = "android.gradle")

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(platform(Firebase.bom))
    implementation(Firebase.authentication)
    implementation("com.google.firebase:firebase-analytics")

    implementation(Kotlin.stdlib.jdk7)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.activity.ktx)
    implementation(AndroidX.constraintLayout)
    implementation(Google.android.material)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.lifecycle.runtime)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha05")
    implementation(AndroidX.lifecycle.runtime.compose)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.lifecycle.viewModel)
    implementation(AndroidX.lifecycle.viewModelCompose)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.navigation.compose)
    implementation(AndroidX.compose.ui.util)
    implementation(COIL.compose)
    implementation(COIL.base)
    implementation(KotlinX.serialization.json)
    //braze
    implementation("com.braze:android-sdk-ui:24.2.0")
    implementation("com.braze:android-sdk-location:24.2.0")
    implementation("com.movableink.sdk:inked:1.0.0")
    // firebase messaging
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-messaging:23.1.2")
    // testing dependencies
    testImplementation(Testing.junit4)
    testImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.core)
    androidTestImplementation(AndroidX.archCore.testing)
    androidTestImplementation(Testing.mockito.core)
    androidTestImplementation(Testing.mockito.android)
    androidTestImplementation(Testing.mockito.kotlin)
    androidTestImplementation(AndroidX.test.runner)
    androidTestImplementation(AndroidX.test.rules)
    androidTestImplementation(AndroidX.test.espresso.core)
}
