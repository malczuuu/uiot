plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.spring.boot.dependencies))
    api(platform(libs.problem4j.spring.bom))
    api(platform(libs.springdoc.openapi.bom))

    constraints {
        api(libs.commons.beanutils)
        api(libs.commons.io)
    }
}
