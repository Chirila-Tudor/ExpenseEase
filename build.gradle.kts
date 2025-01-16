plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "ro.chirila"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-jersey")
	implementation("org.springframework.boot:spring-boot-starter-quartz")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.session:spring-session-core")
	implementation("org.modelmapper:modelmapper:2.3.8")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.0")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.freemarker:freemarker:2.3.32")
	//implementation ("org.postgresql:postgresql:42.2.10")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	annotationProcessor("org.projectlombok:lombok")


}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}
