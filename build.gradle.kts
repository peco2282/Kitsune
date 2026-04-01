import com.android.build.gradle.LibraryExtension
import java.util.Properties
import java.io.FileInputStream
import org.gradle.jvm.tasks.Jar as JvmJar

plugins {
  id("com.android.library") version Versions.ANDROID_GRADLE_PLUGIN apply false
  kotlin("android") version Versions.KOTLIN_ANDROID apply false
  kotlin("jvm") version Versions.KOTLIN apply false
  `maven-publish`
  `java-library`
}

val props = Properties().also {
  val secretsFile = file("secrets.properties")
  if (secretsFile.exists()) {
    FileInputStream(secretsFile).use(it::load)
  }
}

subprojects {
  apply(plugin = "maven-publish")

  val isAndroid = "android" in project.name
  if (!isAndroid) {
    apply(plugin = "java-library")
    plugins.apply("org.jetbrains.kotlin.jvm")
  } else {
    plugins.apply("com.android.library")
    plugins.apply("org.jetbrains.kotlin.android")
  }

  group = ProjectConfig.GROUP
  version = ProjectConfig.VERSION

  dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
  }

  // A. JVM/Java ライブラリの場合
  val sourcesJarTask: TaskProvider<JvmJar> = if (!isAndroid) {
    tasks.register<JvmJar>("sourcesJar") {
      archiveClassifier.set("sources")
      from(project.the<SourceSetContainer>()["main"].allSource)
    }
  }
  // B. Android ライブラリの場合
  else {
    tasks.register<JvmJar>("sourcesJar") {
      archiveClassifier.set("sources")
      // Androidのmainソースセットを参照
      val android = project.the<LibraryExtension>()
      from(android.sourceSets.getByName("main").java.srcDirs)
      from(android.sourceSets.getByName("main").kotlin.directories)
      from(android.sourceSets.getByName("main").resources.srcDirs)
    }
  }

  afterEvaluate {
    val artifactId = if (project.hasProperty("artifactId")) project.property("artifactId") as String else project.name
    val extension = if (project.hasProperty("extension")) project.property("extension") else null
    val productName = "${rootProject.name.lowercase()}-${artifactId}${if (extension != null) "-$extension" else ""}"

    if (!isAndroid)
      tasks.test {
        useJUnitPlatform()
      }
    if (isAndroid) {
      val copyAarTask = tasks.register<Copy>("copyAarToRootLibs") {
        group = "build"
        description = "Copies the release AAR file to the root outputs/libs directory."

        val targetAarName = "${productName}.aar"

        from(project.layout.buildDirectory.dir("outputs/aar"))
        include("${project.name}-release.aar")

        into(rootProject.layout.buildDirectory.dir("outputs/libs/$version"))

        dependsOn(tasks.named("assembleRelease"))

        rename { targetAarName }
      }

      tasks.named("build").configure {
        dependsOn(copyAarTask)
      }
    } else {
      tasks.withType<Jar> {
        archiveBaseName.set(rootProject.name.lowercase() + "-" + (if (extension != null) "$artifactId-$extension" else artifactId))

        val copyJarToRoot = tasks.register<Copy>("copy${project.name}ToRootLibs") {
          group = "build"
          description = "Copies ${project.name}'s JAR to root outputs/libs."

          // 依存性を設定: JARファイルが生成された後に実行されるようにする
          from(destinationDirectory)
          include(archiveFileName.get())
          into(rootProject.layout.buildDirectory.dir("outputs/libs/$version"))
          dependsOn(this@withType) // 元の Jar タスクに依存
        }

        // buildタスクがこのカスタムコピーを必ず実行するようにする
        tasks.named("build").configure {
          dependsOn(copyJarToRoot)
        }
      }
    }
    val copySourcesJarToRoot = tasks.register<Copy>("copySourcesJarToRootLibs") {
      group = "build"
      description = "Copies ${project.name}'s sources JAR to root outputs/libs."

      // ソースJARの命名規則: kitsune-artifactId-sources.jar
      val sourcesJarName = "${productName}-sources.jar"

      from(sourcesJarTask)
      into(rootProject.layout.buildDirectory.dir("outputs/libs/$version"))
      rename { sourcesJarName }

      dependsOn(sourcesJarTask) // sourcesJarタスクに依存
    }
    tasks.named("build").configure {
      dependsOn(copySourcesJarToRoot)
    }

    publishing {
      publications {
        create<MavenPublication>("mavenJava") {
            from(components[if ("android" !in project.name) "java" else "release"])
          artifact(sourcesJarTask.get())

          val module = productName//"Kitsune-${artifactId}${if (extension != null) "-$extension" else ""}"

          pom {
            // nameとdescriptionにはプロジェクト固有の値を使いたいので、モジュールごとに変えるか、プロジェクト名などを使う
            name.set(module) // モジュール名を追加して区別
            description.set("Helper module. (${project.name})")

            licenses {
              license {
                name.set("Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
              }
            }
            developers {
              developer {
                id.set("peco2282")
                name.set("peco2282")
                email.set("pecop2282@gmail.com")
              }
            }
          }
        }
      }

      repositories {
        maven {
          name = "peco2282"
          url = uri("https://maven.peco2282.com/repository/maven-releases")
          credentials {
            username = props["user"].toString()
            password = props["password"].toString()
          }
        }
        maven {
          name = "peco2282-go"
          url = uri("https://repo.peco2282.com/maven-release/")
          credentials {
            username = props["user"].toString()
            password = props["password"].toString()
          }
        }
        maven {
          name = "localRepository"
          url = rootProject.layout.buildDirectory.dir("outputs/repository").get().asFile.toURI()
        }
        maven {
          name = "test"
          url = uri("http://localhost:8888/maven-release")
          credentials {
            username = "peco2282"
            password = "peco2282"
          }
          isAllowInsecureProtocol = true
        }
      }
    }
  }
}
