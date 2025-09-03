plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.movableInk.moengage"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":msp-api"))

    // Mo-Engage
    implementation(moengage.core)
    implementation(moengage.inapp)
    implementation(moengage.pushKit)
    implementation(moengage.pushAmp)
    implementation(moengage.geofence)
    implementation(moengage.inboxCore)
    implementation(moengage.inboxUi)
    // optionally add this if you are using the core module of Inbox
    implementation(moengage.inboxCore)
    // optionally add this to use the Push Templates feature
    implementation(moengage.richNotification)
    // optionally add this to use the Device Trigger feature
    implementation(moengage.deviceTrigger)
    // optionally add this to use the Push Amp feature
    implementation(moengage.pushAmp)
    // optionally add this to use the InApp feature
    implementation(moengage.inapp)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.test.espresso)
}
