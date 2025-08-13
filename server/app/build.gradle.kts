plugins {
    id("base-conventions")
    application
}

application {
    mainClass.set("org.rsmod.server.app.GameServerKt")
    applicationDefaultJvmArgs = listOf("-XX:AutoBoxCacheMax=65535", "-Xms1g")
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(libs.clikt)
    implementation(libs.guice)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.openrs2.cache)
    implementation(projects.api.cache)
    implementation(projects.api.core)
    implementation(projects.api.gameProcess)
    implementation(projects.api.invPlugin)
    implementation(projects.api.net)
    implementation(projects.api.objPlugin)
    implementation(projects.api.parsers.jackson)
    implementation(projects.api.parsers.json)
    implementation(projects.api.parsers.toml)
    implementation(projects.api.registry)
    implementation(projects.api.shops)
    implementation(projects.content.other.commands)
    implementation(projects.engine.annotations)
    implementation(projects.engine.events)
    implementation(projects.engine.game)
    implementation(projects.engine.map)
    implementation(projects.engine.module)
    implementation(projects.engine.routefinder)
    implementation(projects.engine.plugin)
    implementation(projects.server.install)
    implementation(projects.server.logging)
    implementation(projects.server.services)
    implementation(projects.server.shared)
}

tasks.named<JavaExec>("run") {
    description = "Runs the RS Mod game server"
    workingDir = rootProject.projectDir
}
