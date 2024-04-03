plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.apollographql.apollo3:apollo-compiler:4.0.0-beta.5")
    implementation("com.apollographql.apollo3:apollo-ast:4.0.0-beta.5")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}