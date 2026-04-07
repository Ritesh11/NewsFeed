// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hiltAlias) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.sonar) apply true
}

configure<org.sonarqube.gradle.SonarExtension> {
    properties {
        property("sonar.sources", "./src/main")
        property("sonar.organization", "Ritesh11")
        property("sonar.projectKey", "Ritesh11_NewsFeed")
        property("sonar.projectName", "NewsFeed")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}