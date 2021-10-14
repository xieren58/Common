plugins {
    id("com.android.library")
}
android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        version = 1

    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {}