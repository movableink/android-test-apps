group = "com.movableInk"
plugins {
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.compose.compiler)
}
buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.spotless.gradlePlugin)
        classpath(libs.google.services)
    }
}
allprojects {
    repositories {
    }
}

subprojects {
    afterEvaluate {
        apply(file("../spotless.gradle"))
    }
}

tasks.register<DefaultTask>("hello") {
    group = "Custom"
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
    publishAlways()
}
