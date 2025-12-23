plugins {
    id("com.gradle.develocity").version("3.19")
//    id("de.fayard.refreshVersions") version "0.51.0"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

rootProject.name = "ShoppingCart"
include(":app")
