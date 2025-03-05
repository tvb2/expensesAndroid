// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("room_version", "2.6.0")
    }
}
plugins {
    id("com.android.application") version "8.8.2" apply false
    id("com.android.library") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}