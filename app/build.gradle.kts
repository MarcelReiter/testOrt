import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.anfema.testort"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.anfema.testort"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    configureApplicationProductFlavor("server", ServerFlavor.values().toList())
    configureApplicationProductFlavor("services", ServicesFlavor.values().toList())
}

dependencies {
    implementation(projects.feature1)
    implementation(projects.feature2)

    implementation(libs.lottie)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}


/**
 * Warning: a very similar implementation exists as LibraryExtension
 * see [context(LibraryExtension).configureProductFlavor]
 */
internal fun ApplicationExtension.configureApplicationProductFlavor(
    dimensionName: String,
    flavors: List<Flavor>,
) {
    flavorDimensions.add(dimensionName)
    productFlavors {
        flavors.forEach {
            create(it.id) {
                dimension = dimensionName
                versionNameSuffix = it.versionNameSuffix
                applicationIdSuffix = it.applicationIdSuffix
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
