dependencies {
    api(project(":keystone-api"))
    
    // Adventure serialization
    "compileOnly"(rootProject.libs.adventure.minimessage)
    "compileOnly"(rootProject.libs.adventure.gson)
    "compileOnly"(rootProject.libs.adventure.legacy)
    "compileOnly"(rootProject.libs.adventure.plain)

    "testImplementation"(rootProject.libs.junit.jupiter)
    "testImplementation"(rootProject.libs.adventure.minimessage)
    "testImplementation"(rootProject.libs.adventure.legacy)
    "testImplementation"(rootProject.libs.adventure.plain)
    "testRuntimeOnly"(rootProject.libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
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
