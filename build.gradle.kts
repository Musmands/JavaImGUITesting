plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val imguiVersion = "1.92.0"
val lwjglVersion = "3.4.1"
val oshiVersion = "6.4.10"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // dear imgui
    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    implementation("io.github.spair:imgui-java-app:$imguiVersion")

    // jlwgl + glfw
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation ("org.lwjgl:lwjgl")
    implementation ("org.lwjgl:lwjgl-glfw")
    implementation ("org.lwjgl:lwjgl-opengl")

    runtimeOnly ("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly ("org.lwjgl:lwjgl-glfw::natives-windows")
    runtimeOnly ("org.lwjgl:lwjgl-opengl::natives-windows")

    //Hardware monitoring
    implementation("com.github.oshi:oshi-core:$oshiVersion")
}

tasks.test {
    useJUnitPlatform()
}