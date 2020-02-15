import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
}

kotlin {
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "AuthenticationRemote"
            }
        }
    }

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation(project(":shared:remote:Remote"))
        implementation(project(":shared:SharedCommon"))
        implementation(Deps.kotlin)
        implementation(Deps.kotlin_common)
    }

    sourceSets["androidMain"].dependencies {
        implementation(project(":shared:remote:Remote"))
    }

    sourceSets["iosMain"].dependencies {
        implementation(project(":shared:remote:Remote"))

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${Versions.serializer_version}")

        // HTTP
        implementation("io.ktor:ktor-client-ios:${Versions.ktor_version}")
        implementation("io.ktor:ktor-client-json-native:${Versions.ktor_version}")
        implementation("io.ktor:ktor-client-serialization-iosx64:${Versions.ktor_version}")
    }
}