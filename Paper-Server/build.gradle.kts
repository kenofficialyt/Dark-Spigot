plugins {
    id("io.papermc.paperweight.server")
}

paperweight {
    minecraftVersion.set(project.property("minecraftVersion"))
    
    devBundle.set(project.property("paperVersion"))
}

dependencies {
    "paperweightDevBundle"("io.papermc.paper:paper-server:${project.property("paperVersion")}-mojangmapped")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("-Xlint:-options"))
}
