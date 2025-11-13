dependencies {
    api(project(":keystone-api"))
    implementation(project(":keystone-common"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "keystone"
            pom {
                name.set("keystone")
                description.set("Platform abstraction layer for Minecraft plugins")
            }
        }
    }
}
