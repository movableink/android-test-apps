group = "com.movableInk"

buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
    }
}
allprojects {
    repositories {
        google()
        maven(url = "https://appboy.github.io/appboy-android-sdk/sdk")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
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
