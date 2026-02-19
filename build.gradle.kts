plugins {
    idea
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

configurations.all { resolutionStrategy.cacheChangingModulesFor(0, "seconds") }

allprojects {

    group = "ru.otus"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}