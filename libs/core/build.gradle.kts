import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.paging.common.ktx)
}

publishing {
    val publishProperties = loadProperties(
        rootProject.file("publish.properties").path
    )
    val versionProperties = loadProperties(
        rootProject.file("version.properties").path
    )
    repositories {
        maven(publishProperties["githubRepoUrl"].toString()) {
            credentials {
                username = publishProperties["githubUserName"].toString()
                password = publishProperties["githubToken"].toString()
            }
        }
    }
    publications {
        register<MavenPublication>(name) {
            groupId = publishProperties["groupId"].toString()
            artifactId = publishProperties["artifactId"].toString()
            version = versionProperties["versionName"].toString()

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}