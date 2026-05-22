
plugins {
    id("com.android.library")
}

group = "com.example.flutter_video_info"
version = "1.0"

repositories {
        google()
        mavenCentral()
    }

android {
    namespace = "com.example.flutter_video_info"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        disable += "InvalidPackage"
    }
}
