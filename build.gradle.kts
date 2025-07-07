// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("com.google.gms:google-services:4.3.14")
    }
}

plugins {
    id("com.android.application") version "8.6.0" apply false
}
