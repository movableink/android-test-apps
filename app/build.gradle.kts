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

    // Compose
    implementation(libs.bundles.compose)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
//    implementation(libs.play.services)
    // Browser
    implementation(libs.androidx.browser)

    // SFMC

    implementation(libs.salesforce.mc.sdk)
    implementation(libs.marketingcloudsdk.v810)

    // Movable Ink
    implementation(libs.movableink)
    // Testing
    testImplementation(libs.bundles.test.implementation)
    androidTestImplementation(libs.bundles.android.test.implementation)
}
