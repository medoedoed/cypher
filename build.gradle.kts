plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "medoedoded.ru"
version = "0.1.0"

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.hotmoka:toml4j:0.7.3")
    implementation("info.picocli:picocli:4.7.6")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "PasswordManager"
        )
    }
}

tasks {
    shadowJar {
        archiveFileName.set("cypher.jar")
        mergeServiceFiles()
    }
}

