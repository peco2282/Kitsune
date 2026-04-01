extra["artifactId"] = "logging"
extra["extension"] = "android"

android {
  namespace = group.toString()

  compileSdk = 36

  defaultConfig {
    minSdk = 23
  }

  kotlinOptions {
    jvmTarget = "21"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  testOptions {
    unitTests.isIncludeAndroidResources = true
  }

  publishing {
    singleVariant("release") {
    }
  }
}

dependencies {
  implementation(project(":logging-api"))
}
