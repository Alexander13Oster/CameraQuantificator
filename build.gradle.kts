import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")

        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.2.1")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        debug = true
        ignoreFailures = false
        buildUponDefaultConfig = true // preconfigure defaults
        config = files("../config/detekt.yml")
        // point to your custom config defining rules to run, overwriting default behavior
        baseline = file("../config/detekt-baseline.xml")
        parallel = true
        reports {
            html.enabled = true // observe findings in your browser with structure and code snippets
            xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
            txt.enabled = true
            // similar to the console output, contains issue signature to manually edit baseline files
        }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("0.37.0")
        debug.set(true)
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        additionalEditorconfigFile.set(file("../.editorconfig"))
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}