plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.rain.common"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        ndk {
            //选择要添加的对应 cpu 类型的 .so 库。
            abiFilters.run {
                add("armeabi")
                add("armeabi-v7a")
                add("arm64-v8a")
                add("x86_64")
                add("x86")
            }
        }
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
        manifestPlaceholders.run {
            put("JPUSH_PKGNAME", applicationId ?: "com.rain.common")
            put("JPUSH_APPKEY", "")
            put("JPUSH_CHANNEL", "developer-default")
        }
        flavorDimensions.run {
            add("trail")
            add("xinchen")
        }
    }
    signingConfigs {
        create("common") {
            keyAlias = "common_test"
            keyPassword = "123456"
            storeFile = file("common_test.jks")
            storePassword = "123456"
        }
    }

    buildFeatures {
        dataBinding = true
    }
    productFlavors {
        create("trail") {
            dimension = "trail"
            applicationId = "com.ashermed.ysedc.old"
            //个推应用参数，请填写您申请的 GETUI_APP_ID，GETUI_APP_KEY，GETUI_APP_SECRET 值
            manifestPlaceholders.run {
                put("GETUI_APP_ID", "Vo1XOXC5lo8sM85bnpzrm2")
                put("GETUI_APP_KEY", "e3zAQxUa6y5THy2z9TfB4A")
                put("GETUI_APP_SECRET", "ABXyI2RSLp8DCIUqHJ2or5")
                put("PACKAGE_NAME", applicationId ?: "com.ashermed.ysedc.old")
            }
        }

        create("xinchen") {
            dimension = "xinchen"
            applicationId = "com.ashermed.xc"
            //个推应用参数，请填写您申请的 GETUI_APP_ID，GETUI_APP_KEY，GETUI_APP_SECRET 值
            manifestPlaceholders.run {
                put("GETUI_APP_ID", "lPwPdfRB907zI9OOCXprA1")
                put("GETUI_APP_KEY", "FmbYc2ygPI68PWwKrAXMp2")
                put("GETUI_APP_SECRET", "msAi8nuYzUAEY7SzF2L9I4")
                put("PACKAGE_NAME", applicationId ?: "com.ashermed.xc")
            }
        }

    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("common")
        }
        release {
//            applicationIdSuffix ".release"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("common")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    applicationVariants.all {
        outputs.all out@{
            if (this@out is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName = "${buildType.name}_v${versionCode}.apk"
            }
        }
    }
}

dependencies {
    implementation(project(path = ":baseLib"))

    implementation("com.github.LuckSiege.PictureSelector:picture_library:v2.5.9")
    //图片框架
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.3.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.yqritc:recyclerview-flexibledivider:1.4.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.6")
//    //hilt
//    implementation "com.google.dagger:hilt-android:2.28-alpha"
//    kapt "com.google.dagger:hilt-android-compiler:2.28-alpha"

    implementation("com.google.android.exoplayer:exoplayer:2.15.1")

    implementation("com.alibaba:arouter-api:1.5.2")
    kapt("com.alibaba:arouter-compiler:1.5.2")

    implementation("com.github.lihangleo2:ShadowLayout:3.2.0")

    implementation("com.contrarywind:Android-PickerView:4.1.9")
}