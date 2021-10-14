plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        version = 1
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        dataBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    api(project(path = ":common"))
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    api("com.wang.avi:library:2.1.3")
    //上拉下拉刷新布局
    api("com.scwang.smart:refresh-layout-kernel:2.0.3")
    api("com.scwang.smart:refresh-header-classics:2.0.1")

    api("android.arch.navigation:navigation-fragment-ktx:1.0.0")
    api("android.arch.navigation:navigation-ui-ktx:1.0.0")

    implementation("com.alibaba:arouter-api:1.5.2")
    kapt("com.alibaba:arouter-compiler:1.5.2")
}