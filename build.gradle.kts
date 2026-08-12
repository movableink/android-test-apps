group = "com.movableInk"

buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.kotlin.composePlugin)
        classpath(libs.spotless.gradlePlugin)
        classpath(libs.google.services)
        classpath("com.google.firebase:firebase-appdistribution-gradle:5.0.0")
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
