plugins {
    id("java-platform")
}

dependencies {
    constraints {
        // included due to vulnerability in transitive dependency from spring-kafka
        api(libs.commons.beanutils)

        // included due to vulnerability in transitive dependency from spring-kafka
        api(libs.commons.io)
    }
}
