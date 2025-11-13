dependencies {
    api(project(":keystone-api"))
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
