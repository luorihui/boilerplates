pluginManagement {
	repositories {
		gradlePluginPortal()
	}
}

val rootProjectName: String by settings

println("rootProjectName:%s".format(rootProjectName))
rootProject.name = rootProjectName
