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
@Suppress("DSL_SCOPE_VIOLATION")
dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)

    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.datastore.preferences)

    // Compose
    implementation(libs.bundles.compose)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    // Browser
    implementation(libs.androidx.browser)

    // Movable Ink
    implementation(libs.movableink)
    implementation(project(":msp-api"))
    implementation(project(":msp-braze"))
    implementation(project(":msp-moEngage"))
    implementation(project(":msp-airShip"))
    implementation(project(":msp-appsFlyer"))
    implementation(project(":msp-xtremepush"))
    // Testing
    testImplementation(libs.bundles.test.implementation)
    androidTestImplementation(libs.bundles.android.test.implementation)
}
