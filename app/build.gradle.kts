val poiVersion = "5.2.2"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {

    signingConfigs {
        create("SCT") {
            storeFile = file("/Users/josh/AndroidStudioProjects/SCTstopwatch/SCT_keystore")
            storePassword = project.findProperty("SCT_KEYSTORE_PASSWORD") as String
            keyPassword = project.findProperty("SCT_KEY_PASSWORD") as String
            keyAlias = project.findProperty("SCT_KEY_ALIAS") as String
        }
        create("BT") {
            storeFile = file("/Users/josh/AndroidStudioProjects/SCTstopwatch/BT_keystore")
            storePassword = project.findProperty("BT_KEYSTORE_PASSWORD") as String
            keyPassword = project.findProperty("BT_KEY_PASSWORD") as String
            keyAlias = project.findProperty("BT_KEY_ALIAS") as String
        }
    }
    namespace = "com.stopwatch.SCT"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stopwatch.SCT"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += listOf("stopwatch")
    productFlavors {
        create("SCT") {
            applicationId = "com.stopwatch.SCT"
            dimension = "stopwatch"
            versionName = "SCT 2.0"
            resValue ("string", "app_name", "SCT Stopwatch")
            resValue("mipmap", "ic_launcher", "@mipmap/sct_launcher")
            resValue("mipmap", "ic_launcher_round", "@mipmap/sct_launcher_round")
            resValue("mipmap", "ic_launcher_monochrome", "@mipmap/sct_monochrome")
            signingConfig = signingConfigs.getByName("SCT")
        }
        create("BT") {
            applicationId = "com.stopwatch.BT"
            dimension = "stopwatch"
            versionName = "BT 2.0"
            resValue ("string", "app_name", "BT Stopwatch")
            resValue("mipmap", "ic_launcher", "@mipmap/bt_launcher")
            resValue("mipmap", "ic_launcher_round", "@mipmap/bt_launcher_round")
            resValue("mipmap", "ic_launcher_monochrome", "@mipmap/bt_monochrome")
            signingConfig = signingConfigs.getByName("BT")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    implementation(libs.androidx.preference.ktx)
    }
