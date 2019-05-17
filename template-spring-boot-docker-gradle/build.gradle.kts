import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.util.Properties
import java.io.*

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
    if (backupDir.exists()) {
        println("Backup folder exists, delete it first.")
        backupDir.deleteRecursively()
    }
    
    backupDir.mkdirs()
    File("src").copyRecursively(File(backupDir, "src"))
    File("build.gradle.kts").copyTo(File(backupDir, "build.gradle.kts"))
    File(".env").copyTo(File(backupDir, ".env"))
}

fun updateEnv(gradleProperties: Properties) {
    println("Update .env")

    val imageName = "%s/%s".format(gradleProperties.getProperty("group"), gradleProperties.getProperty("baseName"))
    println("Image name: %s".format(imageName))
    val envProperties = Properties()
    
    envProperties.load(File(".env").inputStream())
    envProperties.setProperty("CONTAINER_IMAGE_NAME", imageName)
    envProperties.store(File(".env").outputStream(), "Auto generated by customize task, do not change it manually")
}

fun renameFolders(gradleProperties: Properties) {
    println("Rename folders ...")
    val pkgFolder = gradleProperties.getProperty("group").replace(".", "/")
    val folderPart = "${pkgFolder}/%s".format(gradleProperties.getProperty("baseName"))
    println("folderPart:%s".format(folderPart))

    val oldFolderPart = "com/sunray/templatespringbootdockergradle"

    // Rename main src folder
    renameSrcFolder(oldFolderPart, folderPart, "src/main/java")

    // Rename test src folder
    renameSrcFolder(oldFolderPart, folderPart, "src/test/java")
}

fun alterJavaFiles(gradleProperties: Properties) {
    val oldPkg = "com.sunray.templatespringbootdockergradle"
    val newPkg = "%s.%s".format(gradleProperties.getProperty("group"), gradleProperties.getProperty("baseName"))
    println("Alter Java files, new package name:%s ...".format(newPkg))
    File("src/").walk().forEach {
        if (it.extension == "java") {
            println("Processing %s".format(it))
            val oldContent = it.readText()
            if (oldContent.contains(oldPkg)) {
                val newContent = oldContent.replace(oldPkg, newPkg)
                val writer = it.bufferedWriter()
                writer.write(newContent)
                writer.close()

                println("Replaced")
            } else {
                println("No change")
            }
        }
    }
}

fun sanityCheck() {
    println("Sanity check ...")
}

tasks.create("customize") {
    group = "Utils"
    description = "Customize the project setting"

    doLast{
        val gradleProperties = Properties()
        gradleProperties.load(File("gradle.properties").inputStream())

        println("Customize this project to the following spec:\n");
	    println("Group:%s".format("${project.group}"))
	    println("Name:%s".format(baseName))
	    println("Version:%s".format(version))
	    println("MainClass:%s".format(mainClassName))
	    println("Root Project Name:%s".format(rootProject.name))


	    configCheck()
	    backup()
        
	    updateEnv(gradleProperties)
        
	    renameFolders(gradleProperties)
        
        alterJavaFiles(gradleProperties)
        
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

fun renameSrcFolder(oldFolderPart: String, folderPart: String, parentFolder: String) {
    val oldFolder = "%s/%s".format(parentFolder, oldFolderPart)
    val newFolder = "%s/%s".format(parentFolder, folderPart)
    println("Rename src folder, old %s, new %s".format(oldFolder, newFolder))

    val oldFolderFile = File(oldFolder)
    if (oldFolderFile.exists()) {
        oldFolderFile.walkBottomUp().forEach {
            val newPath = it.absolutePath.replace(oldFolderPart, folderPart)
            println("%s=>%s".format(it.absolutePath, newPath))

            // Create directories if necessary
            if (it.isDirectory) {
                println("Directory, ignore it")
                return@forEach
            }
            val newFile = File(newPath)

            println("Create parent: %s".format(newFile.parentFile.mkdirs()))
            val success = it.renameTo(newFile)
            println("Moved: %s".format(success))
        }
    } else {
        println("Folder do not exist")
    }
}