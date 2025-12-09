plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
    id("com.google.gms.google-services")
}
apply(from = "android.gradle")

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.material.icons)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)

    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.compose.material)

    implementation(libs.androidx.datastore.preferences)

    // Compose
//    implementation(libs.bundles.compose)

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
