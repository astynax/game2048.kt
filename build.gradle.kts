import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

group = "me.astynax"
version = "1.0-SNAPSHOT"

repositories {
  google()
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
  }
}

kotlin {
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "11"
    }
    withJava()
  }
  sourceSets {
    @Suppress("UNUSED_VARIABLE")
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    @Suppress("UNUSED_VARIABLE")
    val jvmTest by getting
  }
}

compose.desktop {
  application {
    mainClass = "game2048.MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "game2048"
      packageVersion = "1.0.0"
    }
  }
}