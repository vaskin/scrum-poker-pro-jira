val kotlinVersion: String by project
val jacksonKotlinVersion: String by project
val springBootVersion: String by project
val springfoxBootStarterVersion: String by project
val dockerRegistry: String? by project
val projectName: String? by project
val imageTag: String? by project
val registryUser: String? by project
val registryPassword: String? by project
val jdkBaseImage: String? by project
val springSecurityOauth2: String by project
val postgresqlVersion: String by project
val logstashLogbackEncoderVersion: String by project
val wiremockVersion: String by project
val jnaVersion: String by project

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")
    implementation("io.springfox:springfox-boot-starter:$springfoxBootStarterVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.flywaydb:flyway-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")

    testImplementation("com.github.tomakehurst:wiremock-jre8:$wiremockVersion")
    testImplementation("net.java.dev.jna:jna-platform:$jnaVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.testcontainers:postgresql")
}

jib {
    from {
        image = jdkBaseImage
    }
    to {
        image = "$dockerRegistry/$projectName"
        tags = setOf("latest", imageTag)
        auth {
            username = registryUser
            password = registryPassword
        }
    }
    container {
        args = listOf("\$JAVA_OPTS")
        environment = mapOf("JAVA_OPTS" to "-Xms128m -Xmx512m -XX:MaxMetaspaceSize=128m -XX:MaxDirectMemorySize=256m")
    }
}
