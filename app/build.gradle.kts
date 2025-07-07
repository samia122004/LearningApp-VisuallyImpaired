plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.speechrecognition"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.speechrecognition"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true;
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.activity:activity:1.9.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.android.gms:play-services-auth-api-phone:17.5.1")// Add this line
    implementation("com.google.firebase:firebase-database:20.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("androidx.core:core:1.0.0")
    implementation ("androidx.biometric:biometric:1.1.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    /*implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.19.1") // For HLS playback
    implementation("com.google.android.exoplayer:exoplayer-dash:2.19.1") // For DASH playback
    implementation("com.google.android.exoplayer:exoplayer-smoothstreaming:2.19.1")*/
    implementation("androidx.media3:media3-exoplayer:1.4.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.0")
    implementation("androidx.media3:media3-ui:1.4.0")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("com.google.code.gson:gson:2.10.1") // Check for the latest version
    implementation("com.google.android.gms:play-services-safetynet:18.0.1")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")// Use the latest version




    // implementation("androidx.biometric:biometric:1.4.0-alpha01")
}