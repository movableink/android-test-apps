plugins {
    id("com.gradle.enterprise").version("3.13.1")
//    id("de.fayard.refreshVersions") version "0.51.0"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://salesforce-marketingcloud.github.io/MarketingCloudSDK-Android/repository")
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "ShoppingCart"
include(":app")
