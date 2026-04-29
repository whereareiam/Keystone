import org.gradle.api.initialization.resolve.RepositoriesMode

rootProject.name = "Keystone"

pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        mavenCentral()
        mavenLocal()
    }
}

include(":keystone-common")
project(":keystone-common").projectDir = file("keystone-common")

include(":keystone-api")
project(":keystone-api").projectDir = file("keystone-api")

include(":keystone")
project(":keystone").projectDir = file("keystone")
