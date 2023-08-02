plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly ("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.28")
    testCompileOnly ("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.28")

    implementation("io.github.openfeign:feign-okhttp:10.11")
    implementation("io.github.openfeign:feign-gson:10.11")
    implementation("io.github.openfeign:feign-slf4j:10.11")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}