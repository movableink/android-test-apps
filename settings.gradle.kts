pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise").version("3.8")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://appboy.github.io/appboy-android-sdk/sdk")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://maven.xtremepush.com/artifactory/libs-release-local/")
    }
    versionCatalogs {
        create("moengage") {
            from("com.moengage:android-dependency-catalog:5.0.0")
        }
    }
}

rootProject.name = "ShoppingCart"
include(":app")
include(":msp-braze")
include(":msp-api")
include(":msp-moEngage")
include(":msp-airShip")
include(":msp-appsFlyer")
include(":msp-xtremepush")
