import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
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
    implementation(Square.okHttp3.okHttp)
    implementation(Square.okHttp3.loggingInterceptor)
    implementation(COIL.compose)
    implementation(COIL.base)
    implementation(AndroidX.dataStore.preferences)

    implementation("com.braze:android-sdk-ui:24.2.0")
    implementation("com.braze:android-sdk-location:24.2.0")
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

    implementation(
        fileTree("libs/") {
            // You can add as many include or exclude calls as you want
            include("inked-debug.aar")
            // You can also include all files by using a pattern wildcard
            include("*.jar")
        }
    )
}
