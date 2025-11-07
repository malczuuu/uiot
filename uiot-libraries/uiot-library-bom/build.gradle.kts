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
        // version included via libs.spring.boot.dependencies is affected by CVE-2025-48734
        api(libs.commons.beanutils)

        // version included via libs.spring.boot.dependencies is affected by CVE-2024-47554
        api(libs.commons.io)

        // version included via libs.spring.boot.dependencies is affected by CVE-2025-48924
        api(libs.commons.lang3)
    }
}
