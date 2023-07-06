plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
}

android {
    namespace = "com.peterchege.notetakingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.peterchege.notetakingapp"
        minSdk = 24
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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    sourceSets {
        getByName( "debug") {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        getByName("release") {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.foundation:foundation-layout:1.4.3")
    implementation ("androidx.compose.material:material-icons-extended:1.6.0-alpha01")


    // compose destinations
    implementation("io.github.raamcosta.compose-destinations:core:1.9.42-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.9.42-beta")


    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // room
    ksp ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    implementation ("androidx.room:room-paging:2.5.2")


    // datastore (core and preferences)
    implementation ("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")


    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.3.7")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.6.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.0.0")

    //timber
    implementation("com.jakewharton.timber:timber:5.0.1")


    //koin
    implementation ("io.insert-koin:koin-android:3.4.2")
    implementation ("io.insert-koin:koin-core:3.4.2")
    implementation ("io.insert-koin:koin-androidx-compose:3.4.3")
    implementation ("io.insert-koin:koin-androidx-workmanager:3.4.2")


    implementation ("androidx.work:work-runtime-ktx:2.9.0-alpha01")





}