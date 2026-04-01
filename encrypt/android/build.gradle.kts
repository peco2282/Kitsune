extra["artifactId"] = "encrypt"
extra["extension"] = "android"

android {
  namespace = group.toString()

  compileSdk = 36

  defaultConfig {
    minSdk = 23
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
  implementation(project(":encrypt-api"))
}
