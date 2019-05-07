import org.springframework.boot.gradle.tasks.bundling.BootJar

buildscript {
    dependencies {
        classpath("gradle.plugin.com.palantir.gradle.docker:gradle-docker:0.22.0")
    }
}

plugins {
    // Apply the java plugin to add support for Java
    java

    eclipse
    
    id("org.springframework.boot") version "2.1.4.RELEASE"
    
    id("com.palantir.docker") version "0.22.0"
}

apply(plugin = "io.spring.dependency-management")

springBoot {
    mainClassName = "com.sunray.templatespringbootdockergradle.TemplateSpringBootDockerGradleApplication"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

group = "com.sunray"

val myBaseName = "templatespringbootdockergradle"  
tasks.getByName<BootJar>("bootJar") {
    baseName = myBaseName
    version =  "0.0.1-SNAPSHOT"
}

tasks.create<Copy>("unpack") {
    dependsOn(tasks.bootJar)
    from(zipTree(tasks["bootJar"].outputs.files.singleFile))
    into("build/dependency")
}
docker {
    print(tasks.bootJar)
    name = "${project.group}/${myBaseName}"
    copySpec.from(tasks["unpack"].outputs).into("dependency")
    buildArgs(mapOf("DEPENDENCY" to "dependency"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")	
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("redis.clients:jedis:2.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

// In this section you declare where to find the dependencies of your project
repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}
