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
    val properties = loadProperties(
        rootProject.file("publish.properties").path
    )
    repositories {
        maven(properties["githubRepoUrl"].toString()) {
            credentials {
                username = properties["githubUserName"].toString()
                password = properties["githubToken"].toString()
            }
        }
    }
    publications {
        register<MavenPublication>(name) {
            groupId = properties["groupId"].toString()
            artifactId = properties["artifactId"].toString()
            version = properties["versionName"].toString()

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}