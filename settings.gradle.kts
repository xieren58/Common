dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            isAllowInsecureProtocol = true
            setUrl("http://maven.aliyun.com/nexus/content/groups/public/")
        }
        maven { setUrl("https://jitpack.io" )}
    }
}


include(":app")
include(":library")
include(":baseLib")
include(":common")
rootProject.name = "Common"

