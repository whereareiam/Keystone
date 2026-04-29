plugins {
    id("keystone.java-common")
}

dependencies {
    api(project(":keystone-api"))

    // Adventure serialization
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.gson)
    compileOnly(libs.adventure.legacy)
    compileOnly(libs.adventure.plain)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.adventure.minimessage)
    testImplementation(libs.adventure.legacy)
    testImplementation(libs.adventure.plain)
    testRuntimeOnly(libs.junit.platform.launcher)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "keystone-common"
            pom {
                name.set("keystone-common")
                description.set("Common implementations for Keystone")
            }
        }
    }
}
