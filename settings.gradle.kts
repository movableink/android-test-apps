plugins {
    id("com.gradle.enterprise").version("3.8")
//    id("de.fayard.refreshVersions") version "0.51.0"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "ShoppingCart"
include(":app")
