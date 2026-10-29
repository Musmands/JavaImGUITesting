plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.github.spair:imgui-java-binding:1.92.0")
    implementation("io.github.spair:imgui-java-lwjgl3:1.92.0")


    implementation(platform("org.lwjgl:lwjgl-bom:3.4.1"))
    implementation ("org.lwjgl:lwjgl")
    implementation ("org.lwjgl:lwjgl-glfw")
    implementation ("org.lwjgl:lwjgl-opengl")
    runtimeOnly ("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly ("org.lwjgl:lwjgl-glfw::natives-windows")
    runtimeOnly ("org.lwjgl:lwjgl-opengl::natives-windows")
}

tasks.test {
    useJUnitPlatform()
}