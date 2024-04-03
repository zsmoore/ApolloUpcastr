plugins {
    kotlin("jvm") version "1.9.21"
    id("com.apollographql.apollo3").version("4.0.0-beta.5")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.apollographql.apollo3:apollo-runtime:4.0.0-beta.5")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.apollographql.apollo3:apollo-testing-support:4.0.0-beta.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1-Beta")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}

apollo {
    service("service") {
        schemaFile.set(file("src/test/resources/schema.graphqls"))
        packageName.set("com.zachary_moore.apolloupcastr.integration")
        decapitalizeFields.set(true)
        codegenModels.set("experimental_operationBasedWithInterfaces")
        plugin(project(":core"))
    }
}