plugins {
	id("chirp.spring-boot-app")
}

group = "com.abrsoftware"
version = "0.0.1-SNAPSHOT"

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation(projects.user)
	implementation(projects.chat)
	implementation(projects.notification)
	implementation(projects.common)

	implementation(libs.kotlin.reflect)
	implementation(libs.spring.boot.starter.security)

	implementation(libs.spring.boot.starter.data.jpa)
	runtimeOnly(libs.postgresql)
}