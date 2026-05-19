import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

// 1. All plugins are declared and managed at the root level
plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.5" apply false
}

group = "com.oi"
version = "1.0.0"

subprojects {
    // 2. Modern way to apply core plugins to subprojects without 'apply(plugin = ...)'
    plugins.apply("java")
    plugins.apply("application")

    // Modern conditional plugin application
    if (name != "sdk") {
        plugins.apply("com.gradleup.shadow")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        if (project.name != "sdk") {
            implementation(project(":sdk"))
        }

        // Vert.x 5.x LTS stack
        implementation(platform("io.vertx:vertx-stack-depchain:5.0.12"))
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

        implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

        // Utilities
        implementation("com.google.guava:guava:33.1.0-jre")
        implementation("commons-io:commons-io:2.16.1")
        implementation("org.apache.commons:commons-text:1.12.0")
        implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
        implementation("org.quartz-scheduler:quartz:2.4.0-rc2")
        implementation("com.opencsv:opencsv:5.9")
        implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")

        testImplementation("io.vertx:vertx-junit5")
        testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    afterEvaluate {
        if (project.name != "sdk") {
            tasks.withType(ShadowJar::class.java) {
                archiveClassifier.set("fat")
                manifest {
                    attributes(mapOf("Main-Class" to project.findProperty("mainClassName")))
                }
                mergeServiceFiles()
            }
        }
        application {
            mainClass.set(project.findProperty("mainClassName") as? String)
        }
    }
}