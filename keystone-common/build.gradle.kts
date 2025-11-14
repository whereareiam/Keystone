dependencies {
    api(project(":keystone-api"))
    
    // Adventure serialization
    "compileOnly"(rootProject.libs.adventure.minimessage)
    "compileOnly"(rootProject.libs.adventure.gson)
    "compileOnly"(rootProject.libs.adventure.legacy)
    "compileOnly"(rootProject.libs.adventure.plain)
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
