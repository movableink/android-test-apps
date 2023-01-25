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
    implementation(AndroidX.lifecycle.runtime)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.lifecycle.viewModel)
    implementation (AndroidX.activity.compose)
    implementation (AndroidX.compose.ui)
    implementation (AndroidX.compose.ui.tooling)
    implementation (AndroidX.compose.material3)
    implementation (AndroidX.navigation.compose)

    implementation(COIL.base)
    //testing dependencies
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
