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
        property("sonar.organization", "Ritesh11")
        property("sonar.projectKey", "Ritesh11_NewsFeed")
        property("sonar.projectName", "NewsFeed")
        property("sonar.host.url", "https://sonarcloud.io")

        // Fix: Explicitly point to the debug variant
        // This stops Sonar from searching for the 'AppExtension' manually
        property("sonar.android.lint.reportPaths", "app/build/reports/lint-results-debug.xml")

        // Add these to help Sonar find your code and classes
        property("sonar.sources", "app/src/main/java,app/src/main/kotlin")
        property("sonar.binaries", "app/build/intermediates/javac/debug/classes,app/build/tmp/kotlin-classes/debug")

        property("sonar.junit.reportPaths", "app/build/test-results/testDebugUnitTest")
    }
}