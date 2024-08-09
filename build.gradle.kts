import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

group = "com.oi"
version = "1.0.0"

subprojects {

    apply(plugin = "java")
    apply(plugin = "application")

    if (project.name != "sdk") {    //plugin is applied only if the subproject produces executable
        apply(plugin = "com.github.johnrengelman.shadow")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        if (project.name != "sdk") {
            implementation(project(":sdk"))
        }

        implementation(platform("io.vertx:vertx-stack-depchain:4.5.7"))
        implementation("io.vertx:vertx-web-client")
        implementation("io.vertx:vertx-auth-jwt")
        implementation("io.vertx:vertx-web")
        implementation("io.vertx:vertx-web-proxy")
        implementation("io.vertx:vertx-health-check")
        implementation("io.vertx:vertx-hazelcast")
        implementation("io.vertx:vertx-json-schema")
        implementation("io.vertx:vertx-tcp-eventbus-bridge")
        implementation("io.vertx:vertx-config")
        implementation("io.vertx:vertx-mongo-client")
        implementation("io.vertx:vertx-kafka-client")
        implementation("io.vertx:vertx-circuit-breaker")
        implementation("io.vertx:vertx-mail-client")
        implementation("io.vertx:vertx-dropwizard-metrics")
        implementation("io.vertx:vertx-circuit-breaker")
        implementation("io.vertx:vertx-mail-client")

        implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

        // utility functions
        implementation("com.google.guava:guava:33.0.0-jre")
        implementation("commons-io:commons-io:2.15.1")
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")

        implementation("org.quartz-scheduler:quartz:2.3.2")

        implementation("com.opencsv:opencsv:5.9")

        implementation("org.bouncycastle:bcprov-jdk18on:1.77")

        // Vert.x JUnit5 for testing
        testImplementation("io.vertx:vertx-junit5")

        // JUnit Jupiter for testing
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    afterEvaluate {
        if (project.name != "sdk") { //task is configured only if the subproject produces executables.

            tasks.withType(ShadowJar::class.java) {
                archiveClassifier.set("fat")
                manifest {
                    attributes(Pair("Main-Class", project.findProperty("mainClassName")))
                }
                mergeServiceFiles()
            }
        }
        application {
            mainClass.set(project.findProperty("mainClassName") as? String)
        }
    }

}
