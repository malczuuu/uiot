plugins {
    id("java-platform")
}

dependencies {
    constraints {
        api(libs.commons.beanutils)
        api(libs.commons.io)
    }
}
