plugins {
    id("keystone.java-common")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "keystone-api"
            pom {
                name.set("keystone-api")
                description.set("Platform abstraction API for Minecraft plugins")
            }
        }
    }
}
