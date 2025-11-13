allprojects {
    version = (System.getenv("VERSION") ?: "dev")
    group = "me.whereareiam"

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        // general
        "compileOnly"(rootProject.libs.jetbrains.annotations)
        "compileOnly"(rootProject.libs.adventure.api)

        // lombok
        "compileOnly"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)
    }

    extensions.configure<PublishingExtension> {
        repositories {
            mavenLocal()
        }
    }
}
