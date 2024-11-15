import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id(libs.plugins.java.library.get().pluginId)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

val publishProperties = loadProperties(
    rootProject.file("publish.properties").path
)

val versionProperties = loadProperties(
    rootProject.file("version.properties").path
)

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.paging.common.ktx)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        groupId = publishProperties["groupId"].toString(),
        artifactId = publishProperties["artifactId"].toString(),
        version = versionProperties["versionName"].toString()
    )

    pom {
        name = publishProperties["artifactId"].toString()
        description = publishProperties["description"].toString()
        url = publishProperties["repository"].toString()

        licenses {
            license {
                name = publishProperties["licenseName"].toString()
                url = publishProperties["licenseUrl"].toString()
            }
        }

        developers {
            developer {
                id = publishProperties["developerId"].toString()
                name = publishProperties["developerName"].toString()
                email = publishProperties["developerEmail"].toString()
                url = publishProperties["developerUrl"].toString()
            }
        }

        scm {
            url = this@pom.url
            connection = publishProperties["scmConnection"].toString()
            developerConnection = publishProperties["scmDeveloperConnection"].toString()
        }
    }
}