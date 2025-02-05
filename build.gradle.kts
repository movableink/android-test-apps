group = "com.movableInk"

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
        google()
        maven(url = "https://appboy.github.io/appboy-android-sdk/sdk")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://salesforce-marketingcloud.github.io/MarketingCloudSDK-Android/repository")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
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
