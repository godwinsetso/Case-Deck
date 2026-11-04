import java.util.Properties

plugins { id("com.android.application") }

// Release signing: reads keystore.properties (local builds) or environment variables (GitHub Actions).
val ksProps = Properties().apply { val f = rootProject.file("keystore.properties"); if (f.exists()) f.inputStream().use { load(it) } }
fun ks(name: String, env: String): String? = ksProps.getProperty(name) ?: System.getenv(env)

android {
    namespace = "com.godwinsetsoafia.casedeck"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.godwinsetsoafia.casedeck"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            val path = ks("storeFile", "CASEDECK_KEYSTORE_PATH")
            if (path != null) {
                storeFile = file(path)
                storePassword = ks("storePassword", "CASEDECK_KEYSTORE_PASSWORD")
                keyAlias = ks("keyAlias", "CASEDECK_KEY_ALIAS")
                keyPassword = ks("keyPassword", "CASEDECK_KEY_PASSWORD")
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            val rel = signingConfigs.getByName("release")
            if (rel.storeFile != null) signingConfig = rel
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    androidResources { noCompress += listOf("html", "mp4") }
}

dependencies {
    implementation("androidx.webkit:webkit:1.12.1")
}
