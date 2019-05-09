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

val baseName: String by project
val version: String by project
val mainClassName: String by project
val BackupFolder = "_backup"
springBoot {
    mainClassName = mainClassName
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<BootJar>("bootJar") {
    baseName = baseName
    version =  version
}

fun configCheck() {
    println("Config check ...")
}

fun backup() {
    println("Backing up ...")
    val backupDir = File(BackupFolder)
    backupDir.mkdirs()
}

fun updateProperties() {
    println("Update gradle.properties ...")
}

fun updateEnv() {
    println("Update .env")
}

fun renameFolders() {
    println("Rename folders ...")
}

fun alterJavaFiles() {
    println("Alter Java files ...")
}

fun sanityCheck() {
    println("Sanity check ...")
}

tasks.create("customize") {
    group = "Utils"
    description = "Customize the project setting"

    doLast{
	    println("Customize this project to the following spec:\n");
	    println("Group:%s".format("${project.group}"))
	    println("Name:%s".format(baseName))
	    println("Version:%s".format(version))
	    println("MainClass:%s".format(mainClassName))
	    println("Root Project Name:%s".format(rootProject.name))
	    
	    configCheck()
	    backup()
	    updateProperties()
        
	    updateEnv()
        
	    renameFolders()
        
        alterJavaFiles()
        
	    sanityCheck()
        
	    println("Done!")
    }
}

tasks.create<Copy>("unpack") {
    println("unpack config")
    dependsOn(tasks.bootJar)
    from(zipTree(tasks["bootJar"].outputs.files.singleFile))
    into("build/dependency")
    doLast{
        println("unpack doLast")
    }        
}

docker {
    name = "%s/%s".format("${project.group}", baseName)
    copySpec.from(tasks["unpack"].outputs).into("dependency")
    buildArgs(mapOf("DEPENDENCY" to "dependency", "MAINCLASS" to mainClassName))
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
