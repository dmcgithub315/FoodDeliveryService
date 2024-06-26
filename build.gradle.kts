android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    namespace = "com.example.food_delivery_service"
    sourceSets {
        getByName("main") {
            manifest.srcFile("app/src/main/AndroidManifest.xml")
        }
    }

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply true

}

