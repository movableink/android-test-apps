group = "com.movableInk"

buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.12.1")
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
