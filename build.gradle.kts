buildscript {
    val kotlinVersion = "1.6.0"
    repositories {
        maven {
            isAllowInsecureProtocol = true
            setUrl("http://maven.aliyun.com/nexus/content/groups/public/")
        }
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.3")
//        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.28-alpha'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register<Delete>("clean"){
    delete(rootProject.buildDir)
}