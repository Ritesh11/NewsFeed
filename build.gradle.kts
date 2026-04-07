// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hiltAlias) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.sonar) apply true
}

allprojects {
    apply(plugin = "org.sonarqube")

    // This tells the plugin: "Don't try to find Android extensions yourself"
    extensions.configure<org.sonarqube.gradle.SonarExtension> {
        isSkipProject = (project.name != rootProject.name)
    }
}

// Now configure only the ROOT project to collect the data
sonar {
    properties {
        property("sonar.organization", "ritesh11")
        property("sonar.projectKey", "Ritesh11_NewsFeed")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getenv("SONAR_TOKEN") ?: "")

        // Manually collect the files from the app module
        property("sonar.sources", "app/src/main/java,app/src/main/kotlin")
        property("sonar.java.binaries", "app/build/intermediates/javac/debug/classes")
        property("sonar.android.lint.reportPaths", "app/build/reports/lint-results-debug.xml")
    }
}