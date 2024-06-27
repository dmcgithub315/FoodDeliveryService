import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

val sourceCompatibility by extra(VERSION_1_8)
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    namespace = "com.example.food_delivery_service"




}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply true

}

