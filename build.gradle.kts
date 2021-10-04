import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    application
}

group = "me.owner"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("io.ktor:ktor-server-netty:1.6.0")
    implementation("io.ktor:ktor-html-builder:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
    implementation("ch.qos.logback:logback-classic:1.2.5")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "ServerKt"
}