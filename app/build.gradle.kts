plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    compileSdk = 34
    namespace = "org.sabaini.moodtracker"

    defaultConfig {
        applicationId = "org.sabaini.moodtracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        dataBinding = true
    }
    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {

    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support)
    implementation(libs.kotlin.coroutines)
    implementation(libs.test.core)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.calendarview)
    implementation(libs.emoji)
    implementation(libs.hilt)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.splashscreen)

    testImplementation(libs.bundles.common.test)
    androidTestImplementation(libs.bundles.common.android.test)
    
    ksp (libs.room.compiler)
    ksp (libs.hilt.compiler)

    coreLibraryDesugaring (libs.desugar)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}