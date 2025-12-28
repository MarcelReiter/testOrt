import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.cfg.pseudocode.and

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

android {
    namespace = "com.anfema.feature1"

    compileSdk {
        version = release(36)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    configureLibraryProductFlavor("server", ServerFlavor.values().toList())
}

internal fun LibraryExtension.configureLibraryProductFlavor(
    dimensionName: String,
    flavors: List<Flavor>,
) {
    flavorDimensions.add(dimensionName)
    productFlavors {
        flavors.forEach {
            create(it.id) {
                dimension = dimensionName
            }
        }
    }
}

internal sealed interface Flavor {
    val id: String
    val versionNameSuffix: String?
    val applicationIdSuffix: String?
}

internal enum class ServerFlavor(
    override val id: String,
    override val versionNameSuffix: String?,
    override val applicationIdSuffix: String?,
) : Flavor {
    Tui(
        id = "tui",
        versionNameSuffix = "_tui",
        applicationIdSuffix = ".tui"
    ),
    PreLive(
        id = "prelive",
        versionNameSuffix = "_prelive",
        applicationIdSuffix = ".prelive"
    ),
    Live(
        id = "live",
        versionNameSuffix = null,
        applicationIdSuffix = ".live"
    )
}

internal enum class ServicesFlavor(
    override val id: String,
    override val versionNameSuffix: String?,
    override val applicationIdSuffix: String?,
) : Flavor {
    Google(
        id = "google",
        versionNameSuffix = "_google",
        applicationIdSuffix = null
    ),
    Huawei(
        id = "huawei",
        versionNameSuffix = "_huawei",
        applicationIdSuffix = null
    )
}
