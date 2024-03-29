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

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    api("androidx.core:core-ktx:1.3.2")
    api("androidx.appcompat:appcompat:1.3.0")
    api("com.google.android.material:material:1.3.0")

    //ali-oss
    api("com.aliyun.dpa:oss-android-sdk:2.9.4")
    //okHttp
    api("com.squareup.okhttp3:okhttp:4.9.1")

    //retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

    api("androidx.exifinterface:exifinterface:1.3.2")

    //dataStore数据存储
    api("androidx.datastore:datastore-preferences:1.0.0")

    //activity类
    api("androidx.activity:activity-ktx:1.4.0-alpha01")
    api("androidx.fragment:fragment-ktx:1.4.0-alpha08")
    //最新版recyclerView
    api("androidx.recyclerview:recyclerview:1.2.1")

    implementation("com.alibaba:arouter-api:1.5.2")
    kapt("com.alibaba:arouter-compiler:1.5.2")
}